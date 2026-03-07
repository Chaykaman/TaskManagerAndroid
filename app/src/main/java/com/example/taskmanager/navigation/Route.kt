package com.example.taskmanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Menu
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable data object Tasks: Route
    @Serializable data object Calendar: Route
    @Serializable data object Menu : Route
}

val TOP_LEVEL_ROUTES = mapOf<NavKey, NavBarItem>(
    Route.Tasks to NavBarItem(icon = Icons.Filled.Checklist, title = "Задачи"),
    Route.Calendar to NavBarItem(icon = Icons.Filled.CalendarMonth, title = "Календарь"),
    Route.Menu to NavBarItem(icon = Icons.Filled.Menu, title = "Обзор"),
)