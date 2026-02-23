package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
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

    // ========== ОБЩАЯ СТАТИСТИКА ==========

    //Всего задач
    suspend fun totalCount(): Int = withContext(Dispatchers.IO) {
        taskDao.countTotal()
    }

    /** Активных / Выполненных */
    suspend fun completionStats(): Pair<Int, Int> = withContext(Dispatchers.IO) {
        taskDao.countActive() to taskDao.countCompleted()
    }

    //Процент выполненых задач
    suspend fun completionRate(): Float = withContext(Dispatchers.IO) {
        val total = taskDao.countTotal()
        if (total == 0) 0f else taskDao.countCompleted() * 100f / total
    }

    // ========== РАСПРЕДЕЛЕНИЕ ==========

    // По статусу
    suspend fun distributionByStatus(): Map<Status, Int> = withContext(Dispatchers.IO) {
        Status.values().associateWith { taskDao.countByStatus(it) }
    }

    // По приоретеам
    suspend fun distributionByPriority(): Map<Priority, Int> = withContext(Dispatchers.IO) {
        Priority.values().associateWith { taskDao.countByPriority(it) }
    }

    // ========== ПРОДУКТИВНОСТЬ ==========

    //Выполнено за период
    suspend fun completedInPeriod(days: Int): List<Pair<LocalDate, Int>> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Pair<LocalDate, Int>>()
        val today = LocalDate.now()

        for (i in (days - 1) downTo 0) {
            val date = today.minusDays(i.toLong())
            val start = date.atStartOfDay()
            val end = date.plusDays(1).atStartOfDay()
            val count = taskDao.countCompletedInPeriod(start, end)
            result.add(date to count)
        }
        result
    }

    //Среднее время задачи в часах
    suspend fun averageCompletionTime(): Float? = withContext(Dispatchers.IO) {
        taskDao.getAverageCompletionTime()?.times(24) // дни → часы
    }
}