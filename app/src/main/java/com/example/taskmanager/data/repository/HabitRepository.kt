package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.data.local.entity.habit.HabitCompletionCount
import com.example.taskmanager.data.local.entity.habit.HabitLog
import com.example.taskmanager.data.local.entity.habit.HabitStatEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    // Привычки
    fun getActiveHabits(): Flow<List<Habit>>
    fun getHabitsForToday(): Flow<List<Habit>>
    fun getArchivedHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: Int): Habit?
    suspend fun addHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun archiveHabit(id: Int)
    suspend fun deleteHabit(habit: Habit)

    // Отметка выполнения
    suspend fun toggleHabitCompletion(habitId: Int, date: LocalDate)
    fun isCompletedOnDate(habitId: Int, date: LocalDate): Flow<Boolean>
    fun getLogsBetween(habitId: Int, start: LocalDate, end: LocalDate): Flow<List<HabitLog>>

    // Статистика
    suspend fun getTotalCompletedCount(habitId: Int): Int
    suspend fun getCompletionRate(habitId: Int): Double
    suspend fun getTopHabits(start: LocalDate, end: LocalDate, limit: Int = 5): List<HabitCompletionCount>
    fun getCompletedDates(habitId: Int): Flow<List<LocalDate>>
    fun getLogsForDate(date: LocalDate): Flow<List<HabitLog>>
    suspend fun getCompletionsPerDay(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DayCompletionCount>
    suspend fun getHabitStatsForPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitStatEntry>

    // Для достижений
    suspend fun getHabitsForDayOnce(dayName: String): List<Habit>
    suspend fun getLogsForDateOnce(date: LocalDate): List<HabitLog>
}