package com.example.taskmanager.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.taskmanager.feature.calendar.CalendarScreen
import com.example.taskmanager.feature.menu.MenuScreen
import com.example.taskmanager.feature.taskcreate.TaskCreateScreen
import com.example.taskmanager.feature.taskdetail.TaskDetailScreen
import com.example.taskmanager.feature.tasklist.TaskListScreen
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.featureTasksSection(
    onTaskClick: (Int) -> Unit,
    onAddTaskClick: (LocalDate?) -> Unit,
    onBack: () -> Unit
) {
    // Страница задач
    entry<Route.Tasks> {
        TaskListScreen(
            onTaskClick = onTaskClick,
            onAddTaskClick = onAddTaskClick
        )
    }

    // Страница задачи
    entry<Route.TaskDetail>(
        metadata = BottomSheetSceneStrategy.bottomSheet(
            dragHandle = {},
            modifier = Modifier.fillMaxHeight()
        )
    ) { key ->
        TaskDetailScreen(
            taskId = key.taskId,
            onBack = onBack
        )
    }

    // Страница добавления задачи
    entry<Route.TaskCreate>(
        metadata = BottomSheetSceneStrategy.bottomSheet(dragHandle = {})
    ) { key ->
        TaskCreateScreen(
            defaultDate = key.defaultDate,
            onBack = onBack
        )
    }
}

fun EntryProviderScope<NavKey>.featureCalendarSection(
    onTaskClick: (Int) -> Unit,
    onAddTaskClick: (LocalDate?) -> Unit,
) {
    entry<Route.Calendar> {
        CalendarScreen(
            onTaskClick = onTaskClick,
            onAddTaskClick = onAddTaskClick
        )
    }
}

fun EntryProviderScope<NavKey>.featureMenuSection() {
    entry<Route.Menu> {
        MenuScreen()
    }
}