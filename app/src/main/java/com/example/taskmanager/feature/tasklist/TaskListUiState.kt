package com.example.taskmanager.feature.tasklist

import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting

data class TaskListUiState(
    val items: List<TaskListItem> = emptyList(),

    val activeFiltering: TaskFiltering = TaskFiltering.ALL,
    val activeSorting: TaskSorting = TaskSorting(),
    val activeGrouping: TaskGrouping = TaskGrouping.NONE,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
