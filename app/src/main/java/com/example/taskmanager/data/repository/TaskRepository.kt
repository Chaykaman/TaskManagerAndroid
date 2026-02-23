package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
import com.example.taskmanager.data.local.entity.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: Dao
) {

    // ========== CRUD ==========

    suspend fun create(task: Task) = taskDao.insert(task)

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun deleteById(id: Int) = taskDao.deleteById(id)

    // ========== ДЛЯ UI ==========

    // Все задачи
    fun allTasks(): Flow<List<Task>> = taskDao.getAll()

    // Вывод активных задач
    fun activeTasks(): Flow<List<Task>> = taskDao.getActive()

    //Вывод только выполненых задач
    fun completedTasks(): Flow<List<Task>> = taskDao.getCompleted()

    //Задачи на сегодня
    fun todayTasks(date: LocalDate): Flow<List<Task>> =
        taskDao.getForToday(date)

    //Вывод задачи на неделю
    fun weekTasks(start: LocalDate, end: LocalDate): Flow<List<Task>> =
        taskDao.getForWeek(start, end)

    // Вывод просроченных задач
    fun overdueTasks(today: LocalDate = LocalDate.now()): Flow<List<Task>> =
        taskDao.getOverdue(today)

    //Поиск
    fun search(query: String): Flow<List<Task>> = taskDao.search(query)

    //Вывод по статусу
    fun byStatus(status: Status): Flow<List<Task>> = taskDao.getByStatus(status)

    //Вывод по приоретету
    fun byPriority(priority: Priority): Flow<List<Task>> = taskDao.getByPriority(priority)

    // ========== БЫСТРЫЕ ДЕЙСТВИЯ ==========

    //Смена статуса когад задача выполняется
    suspend fun complete(taskId: Int) = taskDao.markCompleted(taskId)

    //Изменить статус
    suspend fun setStatus(taskId: Int, status: Status, isDone: Boolean) =
        taskDao.updateStatus(taskId, status, isDone)

    //Получить одну задачу
    suspend fun get(taskId: Int): Task? = taskDao.getById(taskId)
}