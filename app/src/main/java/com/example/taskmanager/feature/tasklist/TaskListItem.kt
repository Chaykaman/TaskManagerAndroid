package com.example.taskmanager.feature.tasklist

import com.example.taskmanager.data.local.entity.Task

sealed class TaskListItem {
    data class Header(val title: String) : TaskListItem()
    data class TaskItem(val task: Task) : TaskListItem()
}