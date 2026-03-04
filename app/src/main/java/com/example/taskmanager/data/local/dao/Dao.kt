package com.example.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface Dao {

    //===========================Create==================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(task: List<Task>)

    //@Insert
    //    suspend fun insertTask(tasks: Tasks)

    //===========================Read (для UI)==================

    //Все задачи
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Task>>

    //По ID
    @Query ("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getById(taskId: Int): Task?

    //===========================Фильтры (для UI)==================

    // Активные (не выполненные)
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY priority ASC, dueDate ASC")
    fun getActive(): Flow<List<Task>>

    // Выполненные
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY updatedAt DESC")
    fun getCompleted(): Flow<List<Task>>

    // На сегодня
    @Query("""
        SELECT * FROM tasks 
        WHERE dueDate = :date AND isCompleted = 0 
        ORDER BY priority ASC, dueTime ASC
    """)
    fun getForToday(date: LocalDate): Flow<List<Task>>

    // На эту неделю
    @Query("""
        SELECT * FROM tasks 
        WHERE dueDate BETWEEN :startDate AND :endDate 
        AND isCompleted = 0 
        ORDER BY dueDate ASC, priority ASC
    """)
    fun getForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<Task>>

    // Просроченные задачи
    @Query("""
        SELECT * FROM tasks 
        WHERE dueDate < :today AND isCompleted = 0 
        ORDER BY dueDate ASC
    """)
    fun getOverdue(today: LocalDate): Flow<List<Task>>


    // Вывод по статусу
    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY dueDate ASC")
    fun getByStatus(status: Status): Flow<List<Task>>

    // По приоритету
    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY dueDate ASC")
    fun getByPriority(priority: Priority): Flow<List<Task>>

    // По поисковой строке
    @Query("""
        SELECT * FROM tasks 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun search(query: String): Flow<List<Task>>

    // ==================== READ (Аналитика) ====================

    // Сколько всего задач не выполнено
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    suspend fun countActive(): Int
    // Сколько всего задач выполнено
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    suspend fun countCompleted(): Int
    //Сколько всего задач есть в базе
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun countTotal(): Int

    // Сколько всего задач по выбранному статусу
    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status")
    suspend fun countByStatus(status: Status): Int

    // Сколько всего задач по выбранному приоретету
    @Query("SELECT COUNT(*) FROM tasks WHERE priority = :priority")
    suspend fun countByPriority(priority: Priority): Int

    // Выполнено задач за период
    @Query("""
        SELECT COUNT(*) FROM tasks 
        WHERE isCompleted = 1 
        AND updatedAt BETWEEN :start AND :end
    """)
    suspend fun countCompletedInPeriod(start: LocalDateTime, end: LocalDateTime): Int

    // Среднее время выполнения
    @Query("""
        SELECT AVG(
            CAST((julianday(updatedAt) - julianday(createdAt)) AS INTEGER)
        ) 
        FROM tasks 
        WHERE isCompleted = 1
    """)
    suspend fun getAverageCompletionTime(): Float?

    // ==================== UPDATE ====================
    @Update
    suspend fun update(task: Task)

    // Обновление по изменения статуса
    @Query("""
        UPDATE tasks 
        SET status = :status, 
            isCompleted = :isCompleted, 
            updatedAt = :now 
        WHERE id = :taskId
    """)
    suspend fun updateStatus(
        taskId: Int,
        status: Status,
        isCompleted: Boolean,
        now: LocalDateTime = LocalDateTime.now()
    )

    // Отметить когда статус становиться "Выполнено"
    @Query("""
        UPDATE tasks 
        SET isCompleted = 1, 
            status = 'Completed', 
            updatedAt = :now 
        WHERE id = :taskId
    """)
    suspend fun markCompleted(
        taskId: Int,
        now: LocalDateTime = LocalDateTime.now()
    )

    // ==================== DELETE ====================

    @Delete
    suspend fun delete(task: Task)


    //Удаление по ID
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteById(taskId: Int)
    //Удаление по выполнености
    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    suspend fun deleteAllCompleted()
    //Полное удаление записей из базы
//    @Query("DELETE FROM tasks")
//    suspend fun deleteAll()

}