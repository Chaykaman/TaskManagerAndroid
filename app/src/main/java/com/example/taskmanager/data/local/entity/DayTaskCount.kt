package com.example.taskmanager.data.local.entity

import java.time.LocalDate

data class DayTaskCount(
    val date: LocalDate,
    val completedCount: Int
)
