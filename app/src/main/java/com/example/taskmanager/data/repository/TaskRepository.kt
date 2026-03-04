package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: Dao
) {

    init {
        TaskLogger.i("TaskRepository создан")
    }

    // ========== CRUD с логированием ==========

    suspend fun create(task: Task): Long {
        TaskLogger.d("Создание задачи: title=${task.title}, priority=${task.priority}")
        val id = taskDao.insert(task)
        TaskLogger.d("Задача создана с ID=$id")
        return id
    }

    suspend fun update(task: Task) {
        TaskLogger.d("Обновление задачи ID=${task.id}, title=${task.title}")
        taskDao.update(task)
        TaskLogger.d("Задача ID=${task.id} обновлена")
    }

    suspend fun delete(task: Task) {
        TaskLogger.d("Удаление задачи ID=${task.id}")
        taskDao.delete(task)
        TaskLogger.d("Задача ID=${task.id} удалена")
    }

    suspend fun deleteById(id: Int) {
        TaskLogger.d("Удаление задачи по ID=$id")
        taskDao.deleteById(id)
        TaskLogger.d("Задача ID=$id удалена")
    }

    // ========== Flow с логированием ==========

    fun allTasks(): Flow<List<Task>> = taskDao.getAll()
        .onEach { list ->
            TaskLogger.d("Загружено ${list.size} задач (все)")
        }

    fun activeTasks(): Flow<List<Task>> = taskDao.getActive()
        .onEach { list ->
            TaskLogger.d("Загружено ${list.size} активных задач")
        }

    fun completedTasks(): Flow<List<Task>> = taskDao.getCompleted()
        .onEach { list ->
            TaskLogger.d("Загружено ${list.size} выполненных задач")
        }

    fun todayTasks(date: LocalDate): Flow<List<Task>> = taskDao.getForToday(date)
        .onEach { list ->
            TaskLogger.d("Загружено ${list.size} задач на сегодня ($date)")
        }

    fun weekTasks(start: LocalDate, end: LocalDate): Flow<List<Task>> =
        taskDao.getForWeek(start, end)
            .onEach { list ->
                TaskLogger.d("Загружено ${list.size} задач на неделю ($start - $end)")
            }

    fun overdueTasks(today: LocalDate): Flow<List<Task>> = taskDao.getOverdue(today)
        .onEach { list ->
            TaskLogger.d("Загружено ${list.size} просроченных задач")
        }

    fun search(query: String): Flow<List<Task>> = taskDao.search(query)
        .onEach { list ->
            TaskLogger.d("Поиск '$query': найдено ${list.size} задач")
        }

    fun byStatus(status: Status): Flow<List<Task>> = taskDao.getByStatus(status)
        .onEach { list ->
            TaskLogger.d("Фильтр по статусу $status: ${list.size} задач")
        }

    fun byPriority(priority: Priority): Flow<List<Task>> = taskDao.getByPriority(priority)
        .onEach { list ->
            TaskLogger.d("Фильтр по приоритету $priority: ${list.size} задач")
        }

    // ========== БЫСТРЫЕ ДЕЙСТВИЯ ==========

    suspend fun complete(taskId: Int) {
        TaskLogger.d("Отметка выполнения задачи ID=$taskId")
        taskDao.markCompleted(taskId)
        TaskLogger.d("Задача ID=$taskId отмечена выполненной")
    }

    suspend fun setStatus(taskId: Int, status: Status, isDone: Boolean) {
        TaskLogger.d("Смена статуса задачи ID=$taskId на $status (completed=$isDone)")
        taskDao.updateStatus(taskId, status, isDone)
        TaskLogger.d("Статус задачи ID=$taskId изменён")
    }

    suspend fun get(taskId: Int): Task? {
        TaskLogger.d("Получение задачи ID=$taskId")
        val task = taskDao.getById(taskId)
        TaskLogger.d(if (task != null) "Задача найдена: ${task.title}" else "Задача не найдена")
        return task
    }
}