package com.example.taskmanager.feature.tasklist

import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting

data class TaskListUiState(
    val items: List<TaskListItem> = emptyList(),

    val activeFilter: TaskFilter = TaskFilter.ALL,
    val activeSorting: TaskSorting = TaskSorting(),
    val activeGrouping: TaskGrouping = TaskGrouping.NONE,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
