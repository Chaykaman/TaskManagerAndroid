package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.HabitDao
import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.data.local.entity.habit.HabitCompletionCount
import com.example.taskmanager.data.local.entity.habit.HabitLog
import com.example.taskmanager.data.local.entity.habit.HabitStatEntry
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    init {
        TaskLogger.i("[HabitRepositoryImpl] Инициализирован")
    }

    /**
     * Получить список активных (не архивированных) привычек.
     */
    override fun getActiveHabits(): Flow<List<Habit>> =
        habitDao.getActiveHabits()

    /**
     * Получить привычки, запланированные на сегодня.
     */
    override fun getHabitsForToday(): Flow<List<Habit>> {
        val today = LocalDate.now().dayOfWeek.name
        return habitDao.getHabitsForDay(today)
    }

    /**
     * Получить список архивированных привычек.
     */
    override fun getArchivedHabits(): Flow<List<Habit>> =
        habitDao.getArchivedHabits()

    /**
     * Получить привычку по её ID.
     * @param id идентификатор привычки
     */
    override suspend fun getHabitById(id: Int): Habit? =
        habitDao.getHabitById(id)

    /**
     * Добавить новую привычку.
     * @param habit сущность привычки
     */
    override suspend fun addHabit(habit: Habit) =
        habitDao.insertHabit(habit)

    /**
     * Обновить существующую привычку.
     * @param habit обновлённая сущность привычки
     */
    override suspend fun updateHabit(habit: Habit) =
        habitDao.updateHabit(habit.copy(updatedAt = LocalDateTime.now()))

    /**
     * Архивировать привычку (логическое удаление).
     * @param id идентификатор привычки
     */
    override suspend fun archiveHabit(id: Int) =
        habitDao.archiveHabit(id)

    /**
     * Полностью удалить привычку и все её логи.
     * @param habit удаляемая привычка
     */
    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteLogsForHabit(habit.id)
        habitDao.deleteHabit(habit)
    }

    /**
     * Переключить выполнение привычки на конкретную дату.
     * Если запись уже есть — удаляет её.
     * Если нет — создаёт новую.
     * @param habitId идентификатор привычки
     * @param date дата выполнения
     */
    override suspend fun toggleHabitCompletion(habitId: Int, date: LocalDate) {
        val existing = habitDao.getLogForDate(habitId, date)
        if (existing != null) {
            habitDao.deleteLogByDate(habitId, date)
        } else {
            habitDao.insertLog(
                HabitLog(
                    habitId = habitId,
                    date = date,
                    isCompleted = true,
                    completedAt = LocalDateTime.now()
                )
            )
        }
    }

    /**
     * Проверить, выполнена ли привычка в конкретную дату.
     * @param habitId идентификатор привычки
     * @param date дата проверки
     */
    override fun isCompletedOnDate(habitId: Int, date: LocalDate): Flow<Boolean> =
        habitDao.isCompletedOnDate(habitId, date)

    /**
     * Получить логи привычки за период.
     * @param habitId идентификатор привычки
     * @param start начало периода
     * @param end конец периода
     */
    override fun getLogsBetween(
        habitId: Int,
        start: LocalDate,
        end: LocalDate
    ): Flow<List<HabitLog>> = habitDao.getLogsBetween(habitId, start, end)

    /**
     * Получить общее количество выполнений привычки.
     * @param habitId идентификатор привычки
     */
    override suspend fun getTotalCompletedCount(habitId: Int): Int =
        habitDao.getTotalCompletedCount(habitId)

    /**
     * Получить процент выполнения привычки.
     * @param habitId идентификатор привычки
     * @return значение от 0.0 до 1.0 (или больше, если логика позволяет)
     */
    override suspend fun getCompletionRate(habitId: Int): Double =
        habitDao.getCompletionRate(habitId)

    /**
     * Получить топ привычек по количеству выполнений за период.
     * @param start начало периода
     * @param end конец периода
     * @param limit максимальное количество привычек
     */
    override suspend fun getTopHabits(
        start: LocalDate,
        end: LocalDate,
        limit: Int
    ): List<HabitCompletionCount> = habitDao.getTopHabits(start, end, limit)

    /**
     * Получить список дат, когда привычка была выполнена.
     * @param habitId идентификатор привычки
     */
    override fun getCompletedDates(habitId: Int): Flow<List<LocalDate>> =
        habitDao.getCompletedDates(habitId)

    /**
     * Получить все логи за конкретную дату.
     * @param date дата
     */
    override fun getLogsForDate(date: LocalDate): Flow<List<HabitLog>> =
        habitDao.getLogsForDate(date)

    /**
     * Получить количество выполнений по дням за период.
     * @param startDate начало периода
     * @param endDate конец периода
     */
    override suspend fun getCompletionsPerDay(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DayCompletionCount> = habitDao.getCompletionsPerDay(startDate, endDate)

    /**
     * Получить статистику по привычкам за период.
     * @param startDate начало периода
     * @param endDate конец периода
     */
    override suspend fun getHabitStatsForPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitStatEntry> = habitDao.getHabitStatsForPeriod(startDate, endDate)

    /**
     * Получить привычки для конкретного дня.
     * @param dayName название дня
     */
    override suspend fun getHabitsForDayOnce(dayName: String): List<Habit> =
        habitDao.getHabitsForDayOnce(dayName)

    /**
     * Получить логи привычки за конкретную дату.
     * @param date дата
     */
    override suspend fun getLogsForDateOnce(date: LocalDate): List<HabitLog> =
        habitDao.getLogsForDateOnce(date)
}