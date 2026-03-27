package com.example.taskmanager.data.local.dao

import androidx.room.*
import androidx.room.Dao
import com.example.taskmanager.data.local.entity.Habit
import com.example.taskmanager.data.local.entity.HabitLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {

    // ============================ CRUD Habit ============================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    // ============================ CRUD HabitLog ============================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)

    @Query("""
        DELETE FROM habitLogs 
        WHERE habitId = :habitId AND date = :date
    """)
    suspend fun deleteLogByDate(habitId: Int, date: LocalDate)

    @Query("""
        DELETE FROM habitLogs 
        WHERE habitId = :habitId
    """)
    suspend fun deleteLogsForHabit(habitId: Int)

    @Query("""
        SELECT * FROM habitLogs 
        WHERE habitId = :habitId
    """)
    suspend fun getLogsForHabit(habitId: Int): List<HabitLog>

    @Query("""
        SELECT * FROM habitLogs 
        WHERE habitId = :habitId
    """)
    fun getLogsForHabitFlow(habitId: Int): Flow<List<HabitLog>>

    // ============================ Query ============================

    @Query("""
        SELECT * FROM habitLogs
        WHERE habitId = :habitId AND date = :date
        LIMIT 1
    """)
    suspend fun getLogForDate(habitId: Int, date: LocalDate): HabitLog?

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM habitLogs 
            WHERE habitId = :habitId 
            AND date = :date 
            AND isCompleted = 1
        )
    """)
    suspend fun isCompletedOnDate(habitId: Int, date: LocalDate): Boolean

    @Query("""
        SELECT * FROM habitLogs
        WHERE habitId = :habitId
        AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getLogsBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<HabitLog>

    // ============================ Stats ============================

    @Query("""
        SELECT COUNT(*) FROM habitLogs
        WHERE habitId = :habitId
        AND isCompleted = 1
    """)
    suspend fun getCompletedCount(habitId: Int): Int

    @Query("""
        SELECT COUNT(*) FROM habitLogs
        WHERE habitId = :habitId
        AND isCompleted = 1
        AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getCompletedCountBetween(
        habitId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Int

    @Query("""
        SELECT MAX(date) FROM habitLogs
        WHERE habitId = :habitId
        AND isCompleted = 1
    """)
    suspend fun getLastCompletedDate(habitId: Int): LocalDate?

    @Query("""
        SELECT date FROM habitLogs
        WHERE habitId = :habitId
        AND isCompleted = 1
        ORDER BY date DESC
    """)
    suspend fun getCompletedDatesDesc(habitId: Int): List<LocalDate>

    @Query("""
        SELECT 
            CASE 
                WHEN COUNT(*) = 0 THEN 0.0
                ELSE COUNT(CASE WHEN isCompleted = 1 THEN 1 END) * 1.0 / COUNT(*)
            END
        FROM habitLogs
        WHERE habitId = :habitId
    """)
    suspend fun getCompletionRate(habitId: Int): Double
}