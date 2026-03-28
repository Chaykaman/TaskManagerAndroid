package com.example.taskmanager.feature.habits.habitform.components

import androidx.compose.runtime.Composable
import com.example.taskmanager.feature.common.SectionHeader

@Composable
fun HabitColorField(
    habitColor: Long,
    onColorSelected: (Long) -> Unit
) {
    SectionHeader(title = "Цвет")

    ColorSelector(
        selectedColor = habitColor,
        onColorSelected = onColorSelected
    )
}