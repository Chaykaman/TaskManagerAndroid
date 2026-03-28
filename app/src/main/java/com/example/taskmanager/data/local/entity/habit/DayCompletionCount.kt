package com.example.taskmanager.data.local.entity.habit

import java.time.LocalDate

data class DayCompletionCount(
    val date: LocalDate,
    val completedCount: Int
)
