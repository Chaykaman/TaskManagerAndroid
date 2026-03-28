package com.example.taskmanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Mood
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed interface Route: NavKey {
    // Задачи
    @Serializable data object Tasks: Route
    @Serializable data class TaskCreate(@Contextual val defaultDate: LocalDate? = null) : Route
    @Serializable data class TaskDetail(val taskId: Int) : Route

    // Календарь
    @Serializable data object Calendar: Route

    // Привычки
    @Serializable data object HabitList: Route
    @Serializable data object MyHabitList : Route
    @Serializable data class HabitForm(@Contextual val habitId: Int? = null) : Route
    @Serializable data object HabitStats : Route

    // Меню
    @Serializable data object Menu: Route
    @Serializable data object Settings : Route

    // Опрос и статистика
    @Serializable data object DailySurvey : Route
    @Serializable data object Statistics : Route

    // Активность и достижения
    @Serializable data object Streak : Route
    @Serializable data object Achievements : Route

}

val TOP_LEVEL_ROUTES = mapOf<NavKey, NavBarItem>(
    Route.Tasks to NavBarItem(icon = Icons.Rounded.Checklist, title = "Задачи"),
    Route.Calendar to NavBarItem(icon = Icons.Rounded.CalendarMonth, title = "Календарь"),
    Route.HabitList to NavBarItem(icon = Icons.Rounded.Mood, title = "Привычки"),
    Route.Menu to NavBarItem(icon = Icons.Rounded.Menu, title = "Обзор"),
)