package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.AchievementDao
import com.example.taskmanager.data.local.entity.achievement.AchievementDefinition
import com.example.taskmanager.data.local.entity.achievement.AchievementDefinitions
import com.example.taskmanager.data.local.entity.achievement.AchievementProgress
import com.example.taskmanager.data.local.entity.achievement.AchievementProgressType
import com.example.taskmanager.data.local.entity.achievement.AchievementTrigger
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.feature.streak.StreakCalculator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@Singleton
class AchievementManager @Inject constructor(
    private val achievementDao: AchievementDao,
    private val taskRepository: TaskRepository,
    private val settingsRepository: AppSettingsRepository,
    private val applicationScope: CoroutineScope
) {

    init {
        TaskLogger.i("[AchievementManager] Инициализирован")
    }

    // Канал для отправки уведомлений о новых достижениях
    private val _newlyUnlocked = MutableSharedFlow<AchievementDefinition>()
    val newlyUnlocked: SharedFlow<AchievementDefinition> = _newlyUnlocked.asSharedFlow()

    // Прогресс для всех достижений при первом запуске
    suspend fun initializeIfNeeded() {
        AchievementDefinitions.all.forEach { definition ->
            achievementDao.insertProgress(
                AchievementProgress(achievementId = definition.id)
            )
        }
    }

    /**
     * Вызывается когда задача создана
     */
    suspend fun onTaskCreated() {
        incrementCounterAchievements(
            trigger = AchievementTrigger.TASK_CREATED
        )
    }

    /**
     * Вызывается когда задача отмечена выполненной
     */
    suspend fun onTaskCompleted() {
        incrementCounterAchievements(
            trigger = AchievementTrigger.TASK_COMPLETED
        )

        // Пересчитываем стрик
        recalculateAndUpdateStreak()
    }

    /**
     * Вызывается когда создана новая привычка
     */
    suspend fun onHabitCreated() {
        incrementCounterAchievements(
            trigger = AchievementTrigger.HABIT_CREATED
        )
    }

    /**
     * Вызывается когда все привычки за день выполнены
     */
    suspend fun onAllHabitsCompleted() {
        val definition = AchievementDefinitions.all.find {
            it.trigger == AchievementTrigger.ALL_HABITS_COMPLETED
        } ?: return

        val existing = achievementDao.getProgress(definition.id)
        if (existing?.isUnlocked == true) return // уже получено

        achievementDao.updateProgressIfNotUnlocked(
            achievementId = definition.id,
            progress = 1,
            isUnlocked = true,
            unlockedAt = LocalDate.now()
        )
        _newlyUnlocked.emit(definition)
    }

    /**
     * Вызывается при изменении стрика
     */
    suspend fun onStreakUpdated(currentStreak: Int) {
        updateStreakAchievements(currentStreak =  currentStreak)
    }

    /**
     * Сброс прогресса
     */
    suspend fun resetAllAchievements() {
        achievementDao.clearAllProgress()
        initializeIfNeeded()
    }

    /**
     * Универсальный метод для достижений.
     * Находит все достижения с нужным trigger, обновляет прогресс
     * и разблокирует те, у которых currentValue >= target.
     */
    private suspend fun incrementCounterAchievements(trigger: AchievementTrigger) {
        val relevantDefinitions = AchievementDefinitions.all.filter {
            it.trigger == trigger && it.progressType is AchievementProgressType.Counter
        }

        relevantDefinitions.forEach { definition ->
            val target = (definition.progressType as AchievementProgressType.Counter).target

            // Получаем текущий прогресс из БД
            val existing = achievementDao.getProgress(definition.id)
                ?: AchievementProgress(achievementId = definition.id)

            // Если уже разблокировано — ничего не делаем
            if (existing.isUnlocked) return@forEach

            // Инкрементируем на 1 — не зависит от текущего состояния таблицы задач
            val newProgress = (existing.currentProgress + 1).coerceAtMost(target)
            val isUnlocked = newProgress >= target

            achievementDao.updateProgressIfNotUnlocked(
                achievementId = definition.id,
                progress = newProgress,
                isUnlocked = isUnlocked,
                unlockedAt = if (isUnlocked) LocalDate.now() else null
            )

            if (isUnlocked) {
                _newlyUnlocked.emit(definition)
            }

            TaskLogger.i(
                "[AchievementManager] Достижение '${definition.title}' | " +
                        "Событие = ${trigger.name} | " +
                        "Прогресс = $newProgress/${definition.progressType.target} | " +
                        "Разблокировано = $isUnlocked")
        }
    }

    /**
     * Метод для обновления прогресса для достижений-стриков
     */
    private suspend fun updateStreakAchievements(currentStreak: Int) {
        val relevantDefinitions = AchievementDefinitions.all.filter {
            it.trigger == AchievementTrigger.STREAK_REACHED &&
                    it.progressType is AchievementProgressType.Counter
        }

        relevantDefinitions.forEach { definition ->
            val target = (definition.progressType as AchievementProgressType.Counter).target
            val isUnlocked = currentStreak >= target
            val progress = currentStreak.coerceAtMost(target)

            val existing = achievementDao.getProgress(definition.id)
                ?: AchievementProgress(achievementId = definition.id)

            // Для стриков обновляем прогресс даже если он уменьшился -
            // но разблокировку не откатываем. Пользователь получил достижение
            // за стрик 10 дней - оно остаётся даже если стрик потом сбросился.
            if (!existing.isUnlocked) {
                achievementDao.updateProgressIfNotUnlocked(
                    achievementId = definition.id,
                    progress = progress,
                    isUnlocked = isUnlocked,
                    unlockedAt = if (isUnlocked) LocalDate.now() else null
                )

                if (isUnlocked) {
                    _newlyUnlocked.emit(definition)
                }
            }
        }
    }

    /**
     * Пересчёт прогресса для достижений-стриков
     */
    private suspend fun recalculateAndUpdateStreak() {
        val minTasks = settingsRepository.streakMinTasks.first()
        val restDays = settingsRepository.streakRestDays.first()

        val yearAgo = LocalDate.now().minusDays(364)
        val today = LocalDate.now()
        val completedPerDay = taskRepository
            .getCompletedTasksPerDay(yearAgo, today)
            .associate { it.date to it.completedCount }

        val streakResult = StreakCalculator.calculate(
            completedPerDay = completedPerDay,
            minTasksPerDay = minTasks,
            restDays = restDays
        )
        onStreakUpdated(streakResult.currentStreak)
    }
}