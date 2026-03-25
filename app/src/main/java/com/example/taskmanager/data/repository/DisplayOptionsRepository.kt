package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting
import kotlinx.coroutines.flow.Flow

interface DisplayOptionsRepository {
    val filtering: Flow<TaskFiltering>
    val sorting: Flow<TaskSorting>
    val grouping: Flow<TaskGrouping>

    suspend fun saveFiltering(filtering: TaskFiltering)
    suspend fun saveSorting(sorting: TaskSorting)
    suspend fun saveGrouping(grouping: TaskGrouping)
}