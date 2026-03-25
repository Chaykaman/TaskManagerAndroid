package com.example.taskmanager.data.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmanager.data.local.entity.Habit
import com.example.taskmanager.data.local.entity.HabitLog
import com.example.taskmanager.data.local.entity.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Query("SELECT * FROM habits")
    suspend fun getAllHabits(): List<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)

    @Query("""
        SELECT * FROM habitLogs
        WHERE habitId = :habitId AND date = :date
        LIMIT 1
    """)
    suspend fun getLogForDate(habitId: Int, date: LocalDate): HabitLog?
}