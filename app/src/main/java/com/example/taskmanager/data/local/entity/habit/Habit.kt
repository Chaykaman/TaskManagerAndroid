package com.example.taskmanager.data.local.entity.habit

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDateTime

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String,
    var description: String = "",

    val iconName: String = "star",
    val color: Long = 0xFF6650A4,

    val frequency: HabitFrequency = HabitFrequency.DAILY,

    val daysOfWeek: List<DayOfWeek> = emptyList(),

    var notificationEnabled: Boolean = false,

    val isArchived: Boolean = false,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)