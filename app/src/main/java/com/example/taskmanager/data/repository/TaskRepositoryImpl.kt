package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.local.entity.SortingDirection
import com.example.taskmanager.data.local.entity.SortingField
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    init {
        TaskLogger.i("[TaskRepositoryImpl] Инициализирован")
    }

    /**
     * Получение списка задач с фильтрацией и сортировкой.
     * @param filtering Фильтр задач (все, активные, выполненные, просроченные).
     * @param sorting Сортировка задач (по возрастанию, по убыванию)
     */
    override fun getTasks(
        filtering: TaskFiltering,
        sorting: TaskSorting
    ): Flow<List<Task>> {
        val tasksFlow = when (filtering) {
            TaskFiltering.ALL -> taskDao.getAllTasks()
            TaskFiltering.ACTIVE -> taskDao.getActiveTasks()
            TaskFiltering.COMPLETED -> taskDao.getCompletedTasks()
            TaskFiltering.OVERDUE -> taskDao.getOverdueTasks(
                date = LocalDate.now().toString(),
                time = LocalTime.now().toString()
            )
        }

        return tasksFlow.map { tasks ->
            val comparator = when (sorting.field) {
                SortingField.ID -> compareBy<Task> { it.id }
                SortingField.TITLE -> compareBy { it.title.lowercase() }
                SortingField.DUE_DATE -> compareBy(nullsLast()) { task -> task.dueDate }
                SortingField.PRIORITY -> compareBy { it.priority.id }
            }
            val directedComparator = when (sorting.direction) {
                SortingDirection.ASC -> comparator
                SortingDirection.DESC -> comparator.reversed()
            }
            tasks.sortedWith(directedComparator)
        }.flowOn(Dispatchers.Default)
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