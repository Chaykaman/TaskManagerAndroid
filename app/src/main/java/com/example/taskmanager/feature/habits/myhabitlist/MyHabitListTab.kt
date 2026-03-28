package com.example.taskmanager.feature.habits.myhabitlist

enum class MyHabitListTab(
    val id: Int,
    val title: String
) {
    ACTIVE(
        id = 1,
        title = "Активные"
    ),
    ARCHIVED(
        id = 2,
        title = "Архивированные"
    )
}