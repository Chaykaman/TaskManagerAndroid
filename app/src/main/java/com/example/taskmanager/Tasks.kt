package com.example.taskmanager

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

enum class Status(val value: Int) {
    Todo(1), InProgress(2), Completed(3)
}

enum class Priority(val value: Int) {
    DoItNow(1), Important(2), Necessary(3), RestoreLater(4)
}

@Entity(tableName = "Tasks")
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    var dueTime: LocalTime,
    var status: Status,
    var createdAt: LocalDate,
    var priority: Priority,
    var notificationEnabled: Boolean,
    var updatedAt: LocalDateTime
)