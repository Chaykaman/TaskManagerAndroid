package com.example.taskmanager.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmanager.feature.calendar.components.DaysOfWeekRow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Entity(tableName = "habits")
data class Habit (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String,
    var description: String = "",

    // Настройки
    var notificationEnabled: Boolean = false,

    // Например: каждый день / через день (на будущее)
    var frequency: String = "DAILY",

    var daysOfWeek: List<DayOfTheWeek>, // 1-7 (Пн-Вс)

    // Дата создания
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()





){

}

