package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.local.entity.SortDirection
import com.example.taskmanager.data.local.entity.SortField
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.data.local.entity.TaskSort
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    init {
        TaskLogger.i("[TaskRepositoryImpl] Инициализирован")
    }

    /**
     * Получение списка задач с фильтрацией и сортировкой.
     * @param filter Фильтр задач (все, активные, выполненные, просроченные).
     * @param sort Сортировка задач (по возрастанию, по убыванию)
     */
    override fun getTasks(
        filter: TaskFilter,
        sort: TaskSort
    ): Flow<List<Task>> {
        val tasksFlow = when (filter) {
            TaskFilter.ALL -> taskDao.getAllTasks()
            TaskFilter.ACTIVE -> taskDao.getActiveTasks()
            TaskFilter.COMPLETED -> taskDao.getCompletedTasks()
            TaskFilter.OVERDUE -> taskDao.getOverdueTasks(LocalDate.now().toString())
        }

        return tasksFlow.map { tasks ->
            val comparator = when (sort.field) {
                SortField.ID -> compareBy<Task> { it.id }
                SortField.TITLE -> compareBy { it.title.lowercase() }
                SortField.DUE_DATE -> compareBy(nullsLast()) { task -> task.dueDate }
                SortField.PRIORITY -> compareBy { it.priority.ordinal }
            }
            val directedComparator = when (sort.direction) {
                SortDirection.ASC -> comparator
                SortDirection.DESC -> comparator.reversed()
            }
            tasks.sortedWith(directedComparator)
        }
    }

    /**
     * Получение списка задач для конкретной даты.
     * @param date Дата для которой нужно получить задачи.
     */
    override fun getTasksForDate(date: LocalDate): Flow<List<Task>> {
        return taskDao.getTasksForDate(date = date.toString())
    }

    /**
     * Получение списка задач в заданном диапазоне дат. Используется для модуля календаря.
     * @param startDate Начальная дата диапазона.
     * @param endDate Конечная дата диапазона.
     */
    override fun getTasksForDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Task>> {
        return taskDao.getTasksForDateRange(
            startDate = startDate.toString(),
            endDate = endDate.toString()
        )
    }

    /**
     * Получение задачи по её идентификатору.
     * @param id Идентификатор задачи.
     */
    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(taskId = id)
    }

    /**
     * Добавление задачи в базу данных.
     * @param task Добавляемая задача.
     */
    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task = task)
        TaskLogger.i("[TaskRepositoryImpl] Задача '${task.title}' (id=${task.id}) добавлена")
    }

    /**
     * Обновление задачи в базе данных.
     * @param task Обновлённая задача.
     */
    override suspend fun updateTask(task: Task) {
        taskDao.insertTask(task = task)
        TaskLogger.i("[TaskRepositoryImpl] Задача '${task.title}' (id=${task.id}) обновлена")
    }

    /**
     * Удаление задачи из базы данных.
     * @param id Идентификатор задачи.
     */
    override suspend fun deleteTask(id: Int) {
        taskDao.deleteTaskById(taskId = id)
        TaskLogger.i("[TaskRepositoryImpl] Задача под номером '${id}' удалена")
    }
}