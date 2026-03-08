package com.example.taskmanager.feature.tasklist

import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.data.local.entity.TaskSort

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val activeFilter: TaskFilter = TaskFilter.ALL,
    val activeSort: TaskSort = TaskSort(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
