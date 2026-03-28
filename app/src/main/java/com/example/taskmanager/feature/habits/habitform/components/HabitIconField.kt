package com.example.taskmanager.feature.habits.habitform.components

import androidx.compose.runtime.Composable
import com.example.taskmanager.feature.common.SectionHeader

@Composable
fun HabitIconField(
    habitIconName: String,
    onIconSelected: (String) -> Unit
) {
    SectionHeader(title = "Иконка")

    IconSelector(
        selectedIconName = habitIconName,
        onIconSelected = onIconSelected
    )
}