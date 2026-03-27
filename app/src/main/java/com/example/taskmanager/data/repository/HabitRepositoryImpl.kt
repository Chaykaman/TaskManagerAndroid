package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.HabitDao
import com.example.taskmanager.data.local.entity.Habit
import com.example.taskmanager.data.local.entity.HabitLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    // ============================  Habit ============================
    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAllHabits()

    override suspend fun getHabitById(id: Int): Habit? =
        habitDao.getHabitById(id)

    override suspend fun addHabit(habit: Habit) =
        habitDao.insertHabit(habit)

    override suspend fun updateHabit(habit: Habit) =
        habitDao.updateHabit(habit)

    override suspend fun deleteHabit(habit: Habit) =
        habitDao.deleteHabit(habit)

    // ============================ HabitLog ============================


    //Проверка есть ли привычек
    override suspend fun toggleHabit(habitId: Int, date: LocalDate) {
        val existing = habitDao.getLogForDate(habitId, date)

        if (existing == null) {
            habitDao.insertLog(
                HabitLog(
                    habitId = habitId,
                    date = date,
                    isCompleted = true
                )
            )
        } else {
            habitDao.insertLog(
                existing.copy(isCompleted = !existing.isCompleted)
            )
        }
    }

    override suspend fun isCompletedOnDate(
        habitId: Int,
        date: LocalDate
    ): Boolean = habitDao.isCompletedOnDate(habitId, date)

    override suspend fun getLogsBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitLog> =
        habitDao.getLogsBetween(habitId, startDate, endDate)

    // ===== STAT =====
    override suspend fun getCompletedCount(habitId: Int): Int =
        habitDao.getCompletedCount(habitId)

    override suspend fun getCompletedCountBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Int =
        habitDao.getCompletedCountBetween(habitId, startDate, endDate)

    override suspend fun getCompletionRate(habitId: Int): Double =
        habitDao.getCompletionRate(habitId)

    override suspend fun getLastCompletedDate(habitId: Int): LocalDate? =
        habitDao.getLastCompletedDate(habitId)

    override suspend fun getStreak(habitId: Int): Int {
        val dates = habitDao.getCompletedDatesDesc(habitId)

        if (dates.isEmpty()) return 0

        var streak = 0
        var currentDate = LocalDate.now()

        for (date in dates) {
            if (date == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }
}