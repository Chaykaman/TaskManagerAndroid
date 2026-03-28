package com.example.taskmanager.feature.habits.habitlist

import com.example.taskmanager.data.local.entity.habit.Habit

data class HabitItem(
    val habit: Habit,
    val isCompletedToday: Boolean
)
