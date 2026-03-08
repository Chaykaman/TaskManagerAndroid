package com.example.taskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    // Название и описание
    var title: String,
    var description: String = "",

    // Дедлайн даты
    var dueDate: LocalDate? = null,
    var dueTime: LocalTime? = null,

    // Статусы и приоритеты
    var status: Status = Status.TODO,
    var priority: Priority = Priority.PRIORITY_4,
    var isCompleted: Boolean = false,

    // Настройки
    var notificationEnabled: Boolean = false,

    // Дата создания и изменения
    var createdAt: LocalDateTime =  LocalDateTime.now(),
    var updatedAt: LocalDateTime =  LocalDateTime.now()
)