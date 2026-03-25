package com.example.taskmanager.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "habitLogs")
data class HabitLog (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val habitId: Int, // связь с Habit

    val date: LocalDate,

    val isCompleted: Boolean = true,

){

}