package com.example.taskmanager.feature.habits.habitform

open class HabitFormMode {
    object Create : HabitFormMode()
    data class Edit(val habitId: Int) : HabitFormMode()
}