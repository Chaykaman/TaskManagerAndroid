package com.example.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmanager.data.local.entity.DayTaskCount
import com.example.taskmanager.data.local.entity.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("""
        SELECT * FROM tasks 
        WHERE isCompleted = 0 AND
        (
            dueDate < :date OR
            (dueDate = :date AND dueTime < :time)
        )
        ORDER BY dueDate ASC, dueTime ASC
    """)
    fun getOverdueTasks(date: String, time: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM tasks WHERE dueDate = :date")
    fun getTasksForDate(date: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :startDate AND :endDate")
    fun getTasksForDateRange(startDate: String, endDate: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("""
    SELECT completedAt as date, COUNT(*) as completedCount
    FROM tasks
    WHERE isCompleted = 1 
    AND completedAt IS NOT NULL
    AND completedAt BETWEEN :startDate AND :endDate
    GROUP BY completedAt
    ORDER BY completedAt ASC
    """)
    suspend fun getCompletedTasksPerDay(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DayTaskCount>

    @Query("""
    SELECT DISTINCT completedAt
    FROM tasks
    WHERE isCompleted = 1
    AND completedAt IS NOT NULL
    ORDER BY completedAt DESC
    """)
    suspend fun getAllCompletedDates(): List<LocalDate>
}