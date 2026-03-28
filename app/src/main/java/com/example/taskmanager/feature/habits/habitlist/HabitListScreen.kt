package com.example.taskmanager.feature.habits.habitlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction

@Composable
fun HabitListScreen(
    viewModel: HabitListViewModel = hiltViewModel(),
    onMyHabitListClick: () -> Unit,
    onHabitStatsClick: () -> Unit,
    onHabitClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenScaffold(
        topBar = {
            ScreenTopAppBar(
                title = "Привычки",
                actions = listOf(
                    TopAppAction(
                        icon = Icons.Rounded.BarChart,
                        contentDescription = "Статистика",
                        onClick = onHabitStatsClick
                    ),
                    TopAppAction(
                        icon = Icons.Rounded.Tune,
                        contentDescription = "Мои привычки",
                        onClick = onMyHabitListClick
                    )
                )
            )
        }
    ) { innerPadding ->
        HabitsContent(
            innerPadding = innerPadding,
            habits = uiState.habits,
            selectedDate = uiState.selectedDate,
            completedHabits = uiState.completedCount,
            totalHabits = uiState.totalCount,
            onHabitClick = onHabitClick,
            onHabitDone = viewModel::toggleHabitCompletion
        )
    }
}