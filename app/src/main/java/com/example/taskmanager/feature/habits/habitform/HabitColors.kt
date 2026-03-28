package com.example.taskmanager.feature.habits.habitform

import androidx.compose.ui.graphics.Color

object HabitColors {

    data class HabitColor(val value: Long, val name: String)

    val available = listOf(
        HabitColor(0xFF6650A4, "Фиолетовый"),
        HabitColor(0xFF4CAF50, "Зелёный"),
        HabitColor(0xFF2196F3, "Синий"),
        HabitColor(0xFFFF5722, "Оранжевый"),
        HabitColor(0xFFE91E63, "Розовый"),
        HabitColor(0xFF00BCD4, "Голубой"),
        HabitColor(0xFFFF9800, "Янтарный"),
        HabitColor(0xFF795548, "Коричневый"),
        HabitColor(0xFF607D8B, "Серо-синий"),
        HabitColor(0xFF9C27B0, "Пурпурный")
    )

    fun getColor(value: Long): Color = Color(value)
}