package com.example.taskmanager.data.local.dao

import androidx.room.*
import androidx.room.Dao
import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.data.local.entity.habit.HabitCompletionCount
import com.example.taskmanager.data.local.entity.habit.HabitLog
import com.example.taskmanager.data.local.entity.habit.HabitStatEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("UPDATE habits SET isArchived = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun archiveHabit(id: Int, updatedAt: LocalDate = LocalDate.now())

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt ASC")
    fun getActiveHabits(): Flow<List<Habit>>

    @Query("""
        SELECT * FROM habits 
        WHERE isArchived = 0
        AND (
            frequency = 'DAILY' 
            OR (frequency = 'SPECIFIC_DAYS' AND daysOfWeek LIKE '%' || :dayName || '%')
        )
        ORDER BY createdAt ASC
    """)
    fun getHabitsForDay(dayName: String): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteLogByDate(habitId: Int, date: LocalDate)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId")
    suspend fun deleteLogsForHabit(habitId: Int)

    @Query("""
        SELECT * FROM habit_logs 
        WHERE habitId = :habitId AND date = :date 
        LIMIT 1
    """)
    suspend fun getLogForDate(habitId: Int, date: LocalDate): HabitLog?

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM habit_logs 
            WHERE habitId = :habitId AND date = :date AND isCompleted = 1
        )
    """)
    fun isCompletedOnDate(habitId: Int, date: LocalDate): Flow<Boolean>

    @Query("""
        SELECT * FROM habit_logs
        WHERE habitId = :habitId
        AND date BETWEEN :startDate AND :endDate
        ORDER BY date ASC
    """)
    fun getLogsBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<HabitLog>>

    // ===================== Статистика =====================

    @Query("""
        SELECT COUNT(*) FROM habit_logs
        WHERE habitId = :habitId AND isCompleted = 1
    """)
    suspend fun getTotalCompletedCount(habitId: Int): Int

    @Query("""
        SELECT 
            CASE WHEN COUNT(*) = 0 THEN 0.0
            ELSE SUM(CASE WHEN isCompleted = 1 THEN 1 ELSE 0 END) * 1.0 / COUNT(*)
            END
        FROM habit_logs
        WHERE habitId = :habitId
    """)
    suspend fun getCompletionRate(habitId: Int): Double

    @Query("""
        SELECT habitId, COUNT(*) as completedDays
        FROM habit_logs
        WHERE isCompleted = 1
        AND date BETWEEN :startDate AND :endDate
        GROUP BY habitId
        ORDER BY completedDays DESC
        LIMIT :limit
    """)
    suspend fun getTopHabits(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int = 5
    ): List<HabitCompletionCount>

    @Query("""
        SELECT date FROM habit_logs
        WHERE habitId = :habitId AND isCompleted = 1
        ORDER BY date DESC
    """)
    fun getCompletedDates(habitId: Int): Flow<List<LocalDate>>

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getLogsForDate(date: LocalDate): Flow<List<HabitLog>>

    @Query("""
    SELECT date, COUNT(*) as completedCount
    FROM habit_logs
    WHERE isCompleted = 1
    AND date BETWEEN :startDate AND :endDate
    GROUP BY date
    ORDER BY date ASC
    """)
    suspend fun getCompletionsPerDay(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DayCompletionCount>

    @Query("""
    SELECT 
        h.id as habitId,
        h.title as habitTitle,
        h.iconName as iconName,
        h.color as color,
        COUNT(hl.id) as completedDays,
        CAST(julianday(:endDate) - julianday(:startDate) + 1 AS INTEGER) as totalDays
    FROM habits h
    LEFT JOIN habit_logs hl 
        ON h.id = hl.habitId 
        AND hl.isCompleted = 1
        AND hl.date BETWEEN :startDate AND :endDate
    WHERE h.isArchived = 0
    GROUP BY h.id
    ORDER BY completedDays DESC
    """)
    suspend fun getHabitStatsForPeriod(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitStatEntry>
}