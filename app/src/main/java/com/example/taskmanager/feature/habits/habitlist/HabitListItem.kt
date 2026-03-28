package com.example.taskmanager.feature.habits.habitlist

import com.example.taskmanager.data.local.entity.habit.Habit

data class HabitListItem(
    val habit: Habit,
    val isCompletedToday: Boolean
)
