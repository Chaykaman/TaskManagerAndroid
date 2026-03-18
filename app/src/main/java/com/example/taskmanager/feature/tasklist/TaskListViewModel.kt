package com.example.taskmanager.feature.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.SortingDirection
import com.example.taskmanager.data.local.entity.SortingField
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.DisplayOptionsRepository
import com.example.taskmanager.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val optionsRepository: DisplayOptionsRepository
) : ViewModel() {

    init {
        TaskLogger.i("[TaskListViewModel] Инициализирован")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TaskListUiState> = combine(
        optionsRepository.filtering,
        optionsRepository.sorting,
        optionsRepository.grouping
    ) { filtering, sorting, grouping ->
        Triple(filtering, sorting, grouping)
    }
        .flatMapLatest { (filtering, sorting, grouping) ->
            taskRepository.getTasks(filtering, sorting)
                .map { tasks ->
                    TaskListUiState(
                        items = groupTasks(tasks, grouping),
                        activeFiltering = filtering,
                        activeSorting = sorting,
                        activeGrouping = grouping
                    )
                }
                .catch { exception ->
                    emit(
                        TaskListUiState(
                            errorMessage = "Не удалось загрузить задачи: ${exception.message}"
                        )
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
     * @param filtering Фильтр задач
     */
    fun setFilter(filtering: TaskFiltering) {
        viewModelScope.launch {
            optionsRepository.saveFiltering(filtering)
        }
    }

    /**
     * Установка группировки задач
     * @param grouping Поле группировки
     */
    fun setGrouping(grouping: TaskGrouping) {
        viewModelScope.launch {
            optionsRepository.saveGrouping(grouping)
        }
    }

    /**
     * Установка сортировки задач
     * @param field Поле сортировки
     */
    fun setSorting(field: SortingField) {
        viewModelScope.launch {
            val currentSorting = uiState.value.activeSorting

            val newSorting = if (currentSorting.field == field) {
                currentSorting.copy(
                    direction = if (currentSorting.direction == SortingDirection.ASC)
                        SortingDirection.DESC else SortingDirection.ASC
                )
            } else {
                TaskSorting(field = field, direction = SortingDirection.ASC)
            }

            optionsRepository.saveSorting(newSorting)
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
    fun addTask(
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
        viewModelScope.launch {
            taskRepository.addTask(newTask)
        }
    }

    /**
     * Обновление статуса выполнения задачи
     * @param task Обновлённая задача
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(
                task = task.copy(isCompleted = !task.isCompleted)
            )
        }
    }

    /**
     * Удаление задачи
     * @param taskId Идентификатор задачи
     */
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.deleteTask(id = taskId)
        }
    }
}