package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.Habit
import com.example.taskmanager.data.local.entity.HabitLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {

    // ============================ Habit ============================

    //Нужен ли Enum ??????????? Нужна ли вообще сортировка ??????
    fun getAllHabits(): Flow<List<Habit>>

    suspend fun getHabitById(id: Int): Habit?
    suspend fun addHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)

    // ============================ HabitLog ============================
    suspend fun toggleHabit(habitId: Int, date: LocalDate)
    suspend fun isCompletedOnDate(habitId: Int, date: LocalDate): Boolean

    suspend fun getLogsBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitLog>

    // ============================  Подсчёт  ============================
    suspend fun getCompletedCount(habitId: Int): Int

    suspend fun getCompletedCountBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Int

    suspend fun getCompletionRate(habitId: Int): Double

    suspend fun getLastCompletedDate(habitId: Int): LocalDate?

    suspend fun getStreak(habitId: Int): Int
}