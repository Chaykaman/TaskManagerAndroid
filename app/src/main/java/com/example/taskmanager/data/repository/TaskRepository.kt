package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.data.local.entity.TaskSort
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getTasks(
        filter: TaskFilter = TaskFilter.ALL,
        sort: TaskSort = TaskSort()
    ): Flow<List<Task>>
    fun getTasksForDate(date: LocalDate): Flow<List<Task>>
    fun getTasksForDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Task>>

    suspend fun getTaskById(id: Long): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Long)
}