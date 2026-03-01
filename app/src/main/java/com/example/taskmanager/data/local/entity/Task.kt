package com.example.taskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

enum class Status(val value: Int, val displayName: String) {
    Todo(1, "К выполнению"),
    InProgress(2, "В процессе"),
    Completed(3, "Выполнено")
}

enum class Priority(val value: Int, val displayName: String) {
    DoItNow(1, "Срочно"),
    Important(2, "Важно"),
    Necessary(3, "Необходимо"),
    RestoreLater(4, "Потом")
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    //Название и описание
    var title: String,
    var description: String = "",

    //Дедлайн даты
    var dueDate: LocalDate? = null,
    var dueTime: LocalTime? = null,

    //Статусы и приорететы
    var status: Status = Status.Todo,
    var priority: Priority = Priority.Necessary,
    var isCompleted: Boolean = false,

    //Настройки
    var notificationEnabled: Boolean = false,

    //Дата создания и измененения
    var createdAt: LocalDateTime =  LocalDateTime.now(),
    var updatedAt: LocalDateTime =  LocalDateTime.now()
)