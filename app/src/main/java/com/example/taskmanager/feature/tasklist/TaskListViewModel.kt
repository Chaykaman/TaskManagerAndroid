package com.example.taskmanager.feature.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.SortingDirection
import com.example.taskmanager.data.local.entity.SortingField
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _activeFilter = MutableStateFlow(TaskFilter.ALL)
    private val _activeSorting = MutableStateFlow(TaskSorting())
    private val _activeGrouping = MutableStateFlow(TaskGrouping.NONE)

    init {
        TaskLogger.i("[TaskListViewModel] Инициализирован")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TaskListUiState> = combine(
        _activeFilter,
        _activeSorting,
        _activeGrouping
    ) { filter, sorting, grouping -> Triple(filter, sorting, grouping) }
        .flatMapLatest { (filter, sorting, grouping) ->
            taskRepository.getTasks(filter, sorting).map { tasks ->
                TaskListUiState(
                    items = groupTasks(tasks, grouping),
                    activeFilter = filter,
                    activeSorting = sorting,
                    activeGrouping = grouping
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskListUiState(isLoading = true)
        )

    private fun groupTasks(tasks: List<Task>, grouping: TaskGrouping): List<TaskListItem> {
        val grouped: Map<String, List<Task>> = when (grouping) {
            TaskGrouping.TITLE -> tasks
                .groupBy { task -> task.title.firstOrNull()?.uppercaseChar()?.toString() ?: "?" }
                .toSortedMap()

            TaskGrouping.DUE_DATE -> tasks
                .groupBy { task -> task.formattedDueDate() }
                .entries
                .sortedBy { it.value.firstOrNull()?.dueDate ?: LocalDate.MAX }
                .associate { it.key to it.value }

            TaskGrouping.PRIORITY -> tasks
                .groupBy { task -> task.priority.label }
                .entries
                .sortedBy { (label, _) -> Priority.entries.first { it.label == label }.id }
                .associate { it.key to it.value }

            TaskGrouping.NONE -> return tasks.map { TaskListItem.TaskItem(it) }
        }

        return grouped.flatMap { (groupTitle, groupTasks) ->
            listOf(TaskListItem.Header(groupTitle)) + groupTasks.map { TaskListItem.TaskItem(it) }
        }
    }

    /**
     * Установка фильтра задач
     * @param filter Фильтр задач
     */
    fun setFilter(filter: TaskFilter) {
        _activeFilter.update { filter }
    }

    /**
     * Установка группировки задач
     * @param grouping Поле группировки
     */
    fun setGrouping(grouping: TaskGrouping) {
        _activeGrouping.update { grouping }
    }

    /**
     * Установка сортировки задач
     * @param field Поле сортировки
     */
    fun setSorting(field: SortingField) {
        _activeSorting.update { currentSorting ->
            if (currentSorting.field == field) {
                currentSorting.copy(
                    direction = if (currentSorting.direction == SortingDirection.ASC)
                        SortingDirection.DESC else SortingDirection.ASC
                )
            } else {
                TaskSorting(field = field, direction = SortingDirection.ASC)
            }
        }
    }

    /**
     * Добавление новой задачи
     * @param title Заголовок задачи
     * @param description Описание задачи
     * @param priority Приоритет задачи
     * @param dueDate Дата выполнения задачи
     * @param dueTime Время выполнения задачи
     */
    suspend fun addTask(
        title: String,
        description: String = "",
        priority: Priority = Priority.PRIORITY_4,
        dueDate: LocalDate? = null,
        dueTime: LocalTime? = null
    ) {
        if (title.isBlank()) return

        // Объект новой задачи
        val newTask = Task(
            title = title.trim(),
            description = description.trim(),
            priority = priority,
            dueDate = dueDate,
            dueTime = dueTime
        )

        // Добавляем задачу
        taskRepository.addTask(newTask)
    }

    /**
     * Обновление статуса выполнения задачи
     * @param task Обновлённая задача
     */
    suspend fun toggleTaskCompletion(task: Task) {
        taskRepository.updateTask(
            task = task.copy(isCompleted = !task.isCompleted)
        )
    }

    /**
     * Удаление задачи
     * @param taskId Идентификатор задачи
     */
    suspend fun deleteTask(taskId: Int) {
        taskRepository.deleteTask(id = taskId)
    }
}