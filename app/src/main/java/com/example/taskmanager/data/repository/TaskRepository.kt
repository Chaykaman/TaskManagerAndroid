package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.DayTaskCount
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.data.local.entity.TaskSorting
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getTasks(
        filtering: TaskFiltering = TaskFiltering.ALL,
        sorting: TaskSorting = TaskSorting()
    ): Flow<List<Task>>
    fun getTasksForDate(date: LocalDate): Flow<List<Task>>
    fun getTasksForDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Task>>

    suspend fun getTaskById(id: Int): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Int)

    // Для стриков
    suspend fun getAllCompletedDates(): List<LocalDate>
    suspend fun getCompletedTasksPerDay(startDate: LocalDate, endDate: LocalDate): List<DayTaskCount>
}