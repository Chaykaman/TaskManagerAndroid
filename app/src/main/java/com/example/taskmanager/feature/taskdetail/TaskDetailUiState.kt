package com.example.taskmanager.feature.taskdetail

import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Task
import java.time.LocalDate
import java.time.LocalTime

data class TaskDetailUiState(
    val originalTask: Task? = null,

    // Черновик
    val draftTitle: String = "",
    val draftDescription: String = "",
    val draftIsCompleted: Boolean = false,
    val draftPriority: Priority = Priority.PRIORITY_4,
    val draftDueDate: LocalDate? = null,
    val draftDueTime: LocalTime? = null,

    // Флаг, означающий, что есть несохраненные изменения
    val hasUnsavedChanges: Boolean = false,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)