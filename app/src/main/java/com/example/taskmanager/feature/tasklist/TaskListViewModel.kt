package com.example.taskmanager.feature.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.SortDirection
import com.example.taskmanager.data.local.entity.SortField
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.data.local.entity.TaskSort
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
    private val _activeSort = MutableStateFlow(TaskSort())

    init {
        TaskLogger.i("[TaskListViewModel] Инициализирован")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TaskListUiState> = combine(
        _activeFilter,
        _activeSort
    ) { filter, sort -> Pair(filter, sort) }
        .flatMapLatest { (filter, sort) ->
            taskRepository.getTasks(filter, sort).map { tasks ->
                TaskListUiState(
                    tasks = tasks,
                    activeFilter = filter,
                    activeSort = sort
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskListUiState(isLoading = true)
        )

    /**
     * Установка фильтра задач
     * @param filter Фильтр задач
     */
    fun setFilter(filter: TaskFilter) {
        _activeFilter.update { filter }
    }

    /**
     * Установка поля для сортировки задач
     * @param field Поле для сортировки
     */
    fun onSortFieldSelected(field: SortField) {
        _activeSort.update { currentSort ->
            if (currentSort.field == field) {
                currentSort.copy(
                    direction = if (currentSort.direction == SortDirection.ASC)
                        SortDirection.DESC else SortDirection.ASC
                )
            } else {
                TaskSort(field = field, direction = SortDirection.ASC)
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