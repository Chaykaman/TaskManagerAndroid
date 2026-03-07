package com.example.taskmanager.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.taskmanager.feature.calendar.CalendarScreen
import com.example.taskmanager.feature.menu.MenuScreen
import com.example.taskmanager.feature.tasklist.TaskListScreen

fun EntryProviderScope<NavKey>.featureTasksSection() {
    entry<Route.Tasks> {
        TaskListScreen()
    }
}

fun EntryProviderScope<NavKey>.featureCalendarSection() {
    entry<Route.Calendar> {
        CalendarScreen()
    }
}

fun EntryProviderScope<NavKey>.featureMenuSection() {
    entry<Route.Menu> {
        MenuScreen()
    }
}