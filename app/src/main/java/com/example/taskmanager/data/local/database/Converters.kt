package com.example.taskmanager.data.local.database

import androidx.room.TypeConverter
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Converters {

    @TypeConverter
    fun fromStatus(status: Status): Int = status.id

    @TypeConverter
    fun toStatus(value: Int): Status = Status.entries.first { it.id == value }

    @TypeConverter
    fun fromPriority(priority: Priority): Int = priority.id

    @TypeConverter
    fun toPriority(value: Int): Priority = Priority.entries.first { it.ordinal == value }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? = value?.let { LocalTime.parse(it) }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}