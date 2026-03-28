package com.example.taskmanager.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.taskmanager.feature.achievements.AchievementsScreen
import com.example.taskmanager.feature.calendar.CalendarScreen
import com.example.taskmanager.feature.menu.MenuScreen
import com.example.taskmanager.feature.taskcreate.TaskCreateScreen
import com.example.taskmanager.feature.taskdetail.TaskDetailScreen
import com.example.taskmanager.feature.tasklist.TaskListScreen
import java.time.LocalDate
import com.example.taskmanager.feature.analytics.DailySurveyScreen
import com.example.taskmanager.feature.analytics.StatisticsScreen
import com.example.taskmanager.feature.appsettings.AppSettingsScreen
import com.example.taskmanager.feature.habits.habitform.HabitFormScreen
import com.example.taskmanager.feature.habits.habitlist.HabitListScreen
import com.example.taskmanager.feature.habits.habitstats.HabitStatsScreen
import com.example.taskmanager.feature.habits.myhabitlist.MyHabitsScreen
import com.example.taskmanager.feature.productivity.ProductivityScreen

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
fun EntryProviderScope<NavKey>.featureHabitsSection(
    onHabitFormClick: () -> Unit,
    onMyHabitListClick: () -> Unit,
    onHabitStatsClick: () -> Unit,
    onHabitClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    // Страница привычек на сегодня
    entry<Route.HabitList> {
        HabitListScreen(
            onMyHabitListClick = onMyHabitListClick,
            onHabitStatsClick = onHabitStatsClick,
            onHabitClick = onHabitClick
        )
    }

    // Страница привычки (создание и редактирование)
    entry<Route.HabitForm> { key ->
        HabitFormScreen(
            habitId = key.habitId,
            onBack = onBack
        )
    }

    // Страница привычек пользователя с настройкой
    entry<Route.MyHabitList> {
        MyHabitsScreen(
            onAddHabitClick = onHabitFormClick,
            onHabitClick = onHabitClick,
            onBack = onBack
        )
    }

    // Страница статистики привычек
    entry<Route.HabitStats> {
        HabitStatsScreen(
            onBack = onBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.featureMenuSection(
    onSurveyClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onProductivityClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onAppSettingsClick: () -> Unit
) {
    entry<Route.Menu> {
        MenuScreen(
            onSurveyClick = onSurveyClick,
            onStatisticsClick = onStatisticsClick,
            onProductivityClick = onProductivityClick,
            onAchievementsClick = onAchievementsClick,
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

fun EntryProviderScope<NavKey>.featureProductivitySection(
    onBack: () -> Unit,
) {
    // Продуктивность
    entry<Route.Productivity> {
        ProductivityScreen(
            onBack = onBack
        )
    }

    // Достижения
    entry<Route.Achievements> {
        AchievementsScreen(
            onBack = onBack
        )
    }
}