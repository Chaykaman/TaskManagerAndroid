package com.example.taskmanager.feature.taskdetail

import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // Состояние UI
    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        TaskLogger.i("[TaskDetailViewModel] Инициализирован")
    }

    /**
     * Загрузка задачи по её идентификатору
     * @param taskId Идентификатор задачи
     */
    suspend fun loadTask(taskId: Int) {
        // Получаем задачу
        val task = taskRepository.getTaskById(id = taskId)

        // Если задачи не существует
        if (task == null) {
            _uiState.update {
                it.copy(errorMessage = "Задача не найдена")
            }
            return
        }

        // Черновик = оригинал, пользовательских изменений нет
        _uiState.update {
            it.copy(
                originalTask = task,
                draftTitle = task.title,
                draftDescription = task.description,
                draftIsCompleted = task.isCompleted,
                draftPriority = task.priority,
                draftDueDate = task.dueDate,
                draftDueTime = task.dueTime,
                hasUnsavedChanges = false
            )
        }
    }

    /**
     * Проверка на наличие несохранённых изменений
     * @param original Оригинальная задача
     * @param title Заголовок задачи
     * @param description Описание задачи
     * @param isCompleted Статус выполнения задачи
     * @param priority Приоритет задачи
     * @param dueDate Дата выполнения задачи
     * @param dueTime Время выполнения задачи
     */
    private fun checkForUnsavedChanges(
        original: Task?,
        title: String,
        description: String,
        isCompleted: Boolean,
        priority: Priority,
        dueDate: LocalDate?,
        dueTime: LocalTime?
    ): Boolean {
        if (original == null) return false
        return original.title != title ||
                original.description != description ||
                original.isCompleted != isCompleted ||
                original.priority != priority ||
                original.dueDate != dueDate ||
                original.dueTime != dueTime
    }

    /**
     * Обновление заголовка задачи
     * @param newTitle Новое значение заголовка
     */
    fun onTitleChanged(newTitle: String) {
        _uiState.update { currentState ->
            currentState.copy(
                draftTitle = newTitle,
                hasUnsavedChanges = checkForUnsavedChanges(
                    original = currentState.originalTask,
                    title = newTitle,
                    description = currentState.draftDescription,
                    isCompleted = currentState.draftIsCompleted,
                    priority = currentState.draftPriority,
                    dueDate = currentState.draftDueDate,
                    dueTime = currentState.draftDueTime
                )
            )
        }
    }

    /**
     * Обновление описания задачи
     * @param newDescription Новое значение описания
     */
    fun onDescriptionChanged(newDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                draftDescription = newDescription,
                hasUnsavedChanges = checkForUnsavedChanges(
                    original = currentState.originalTask,
                    title = currentState.draftTitle,
                    description = newDescription,
                    isCompleted = currentState.draftIsCompleted,
                    priority = currentState.draftPriority,
                    dueDate = currentState.draftDueDate,
                    dueTime = currentState.draftDueTime
                )
            )
        }
    }

    /**
     * Обновление статуса выполнения задачи
     * @param isCompleted Новое значение статуса выполнения
     */
    fun onCompletedChanged(isCompleted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                draftIsCompleted = isCompleted,
                hasUnsavedChanges = checkForUnsavedChanges(
                    original = currentState.originalTask,
                    title = currentState.draftTitle,
                    description = currentState.draftDescription,
                    isCompleted = isCompleted,
                    priority = currentState.draftPriority,
                    dueDate = currentState.draftDueDate,
                    dueTime = currentState.draftDueTime
                )
            )
        }
    }

    /**
     * Обновление приоритета задачи
     * @param priority Новое значение приоритета
     */
    fun onPriorityChanged(priority: Priority) {
        _uiState.update { currentState ->
            currentState.copy(
                draftPriority = priority,
                hasUnsavedChanges = checkForUnsavedChanges(
                    original = currentState.originalTask,
                    title = currentState.draftTitle,
                    description = currentState.draftDescription,
                    isCompleted = currentState.draftIsCompleted,
                    priority = priority,
                    dueDate = currentState.draftDueDate,
                    dueTime = currentState.draftDueTime
                )
            )
        }
    }

    /**
     * Обновление даты выполнения задачи
     * @param date Новая дата выполнения
     */
    fun onDateSelected(date: LocalDate?) {
        _uiState.update { currentState ->
            currentState.copy(
                draftDueDate = date,
                draftDueTime = if (date == null) null else currentState.draftDueTime,

                hasUnsavedChanges = checkForUnsavedChanges(
                    original = currentState.originalTask,
                    title = currentState.draftTitle,
                    description = currentState.draftDescription,
                    isCompleted = currentState.draftIsCompleted,
                    priority = currentState.draftPriority,
                    dueDate = date,
                    dueTime = currentState.draftDueTime
                )
            )
        }
    }

    /**
     * Обновление времени выполнения задачи
     * @param time Новое время выполнения
     */
    fun onTimeSelected(time: LocalTime?) {
        _uiState.update { currentState ->
            currentState.copy(
                draftDueTime = time,
                draftDueDate = if (time != null && currentState.draftDueDate == null)
                    LocalDate.now() else currentState.draftDueDate,

                hasUnsavedChanges = checkForUnsavedChanges(
                    original = currentState.originalTask,
                    title = currentState.draftTitle,
                    description = currentState.draftDescription,
                    isCompleted = currentState.draftIsCompleted,
                    priority = currentState.draftPriority,
                    dueDate = currentState.draftDueDate,
                    dueTime = time
                )
            )
        }
    }

    /**
     * Сохранение изменений
     */
    suspend fun saveChanges() {
        val currentState = _uiState.value
        val original = currentState.originalTask ?: return

        if (currentState.draftTitle.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Заголовок не может быть пустым")
            }
            return
        }

        // Обновлённая задача
        val updatedTask = original.copy(
            title = currentState.draftTitle.trim(),
            description = currentState.draftDescription.trim(),
            isCompleted = currentState.draftIsCompleted,
            priority = currentState.draftPriority,
            dueDate = currentState.draftDueDate,
            dueTime = currentState.draftDueTime
        )

        // Обновляем задачу
        taskRepository.updateTask(task = updatedTask)

        // Обновляем UI и сбрасываем флаг
        _uiState.update {
            it.copy(
                originalTask = updatedTask,
                hasUnsavedChanges = false
            )
        }
    }

    /**
     * Удаление несохранённых изменений
     */
    fun discardChanges() {
        val original = _uiState.value.originalTask ?: return

        // Возвращение к оригинальным значениям
        _uiState.update {
            it.copy(
                draftTitle = original.title,
                draftDescription = original.description,
                draftIsCompleted = original.isCompleted,
                draftPriority = original.priority,
                draftDueDate = original.dueDate,
                draftDueTime = original.dueTime,
                hasUnsavedChanges = false
            )
        }
    }
}