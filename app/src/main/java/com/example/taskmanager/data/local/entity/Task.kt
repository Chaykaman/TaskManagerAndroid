package com.example.taskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
) {
    fun formattedDueDate(): String {
        val date = dueDate ?: return "Без даты"

        val today = LocalDate.now()
        return when (date) {
            today -> "Сегодня"
            today.plusDays(1) -> "Завтра"
            today.minusDays(1) -> "Вчера"
            else -> {
                val formatter = if (date.year == today.year) {
                    DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))
                } else {
                    DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"))
                }
                date.format(formatter)
            }
        }
    }

    fun isOverdue(): Boolean {
        if (isCompleted || dueDate == null) return false

        val time = dueTime ?: LocalTime.MAX
        val dueDateTime = LocalDateTime.of(dueDate, time)
        return dueDateTime.isBefore(LocalDateTime.now())
    }
}