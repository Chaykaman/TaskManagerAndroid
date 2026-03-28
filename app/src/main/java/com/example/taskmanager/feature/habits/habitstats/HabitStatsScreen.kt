package com.example.taskmanager.feature.habits.habitstats

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar

@Composable
fun HabitStatsScreen(
    viewModel: HabitStatsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Статистика привычек",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        HabitStatsContent(
            selectedPeriod = uiState.selectedPeriod,
            onPeriodSelected = viewModel::onPeriodSelected,
            overallCompletionRate = uiState.overallCompletionRate,
            totalCompleted = uiState.totalCompleted,
            totalPossible = uiState.totalPossible,
            dailyCompletions = uiState.dailyCompletions,
            habitStats = uiState.habitStats,
            modifier = Modifier.padding(innerPadding)
        )
    }
}