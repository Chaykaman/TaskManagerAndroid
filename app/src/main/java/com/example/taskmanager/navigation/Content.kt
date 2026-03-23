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
import com.example.taskmanager.feature.analytics.DailySurveyScreen
import com.example.taskmanager.feature.analytics.StatisticsScreen
import com.example.taskmanager.feature.appsettings.AppSettingsScreen

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

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.featureMenuSection(
    onSurveyClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onAppSettingsClick: () -> Unit
) {
    entry<Route.Menu> {
        MenuScreen(
            onSurveyClick = onSurveyClick,
            onStatisticsClick = onStatisticsClick,
            onAppSettingsClick = onAppSettingsClick
        )
    }

    // Страница настроек
    entry<Route.Settings>(
        metadata = BottomSheetSceneStrategy.bottomSheet(
            modifier = Modifier.fillMaxHeight()
        )
    ) {
        AppSettingsScreen()
    }
}

fun EntryProviderScope<NavKey>.featureAnalyticsSection(
    onBack: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    entry<Route.DailySurvey> {
        DailySurveyScreen(
            onBack = onBack,
            onFinished = onNavigateToStatistics
        )
    }
    entry<Route.Statistics> {
        StatisticsScreen(onBack = onBack)
    }
}