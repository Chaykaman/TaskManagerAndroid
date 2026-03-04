package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val taskDao: Dao
) {

    init {
        TaskLogger.i("AnalyticsRepository создан")
    }

    // ========== ОБЩАЯ СТАТИСТИКА ==========

    suspend fun totalCount(): Int = withContext(Dispatchers.IO) {
        TaskLogger.d("Подсчёт общего количества задач")
        val count = taskDao.countTotal()
        TaskLogger.d("Всего задач: $count")
        count
    }

    suspend fun completionStats(): Pair<Int, Int> = withContext(Dispatchers.IO) {
        TaskLogger.d("Подсчёт статистики выполнения")
        val active = taskDao.countActive()
        val completed = taskDao.countCompleted()
        TaskLogger.d("Активных: $active, Выполненных: $completed")
        active to completed
    }

    suspend fun completionRate(): Float = withContext(Dispatchers.IO) {
        val total = taskDao.countTotal()
        val rate = if (total == 0) 0f else taskDao.countCompleted() * 100f / total
        TaskLogger.d("Процент выполнения: $rate%")
        rate
    }

    // ========== РАСПРЕДЕЛЕНИЕ ==========

    suspend fun distributionByStatus(): Map<Status, Int> = withContext(Dispatchers.IO) {
        TaskLogger.d("Распределение по статусам")
        val map = Status.values().associateWith { taskDao.countByStatus(it) }
        TaskLogger.d("По статусам: $map")
        map
    }

    suspend fun distributionByPriority(): Map<Priority, Int> = withContext(Dispatchers.IO) {
        TaskLogger.d("Распределение по приоритетам")
        val map = Priority.values().associateWith { taskDao.countByPriority(it) }
        TaskLogger.d("По приоритетам: $map")
        map
    }

    // ========== ПРОДУКТИВНОСТЬ ==========

    suspend fun completedInPeriod(days: Int): List<Pair<LocalDate, Int>> = withContext(Dispatchers.IO) {
        TaskLogger.d("Анализ продуктивности за $days дней")
        val result = mutableListOf<Pair<LocalDate, Int>>()
        val today = LocalDate.now()

        for (i in (days - 1) downTo 0) {
            val date = today.minusDays(i.toLong())
            val start = date.atStartOfDay()
            val end = date.plusDays(1).atStartOfDay()
            val count = taskDao.countCompletedInPeriod(start, end)
            result.add(date to count)
        }

        TaskLogger.d("Продуктивность: ${result.size} записей")
        result
    }

    suspend fun averageCompletionTime(): Float? = withContext(Dispatchers.IO) {
        TaskLogger.d("Расчёт среднего времени выполнения")
        val hours = taskDao.getAverageCompletionTime()?.times(24)
        TaskLogger.d("Среднее время: ${hours ?: "N/A"} часов")
        hours
    }
}