package com.example.taskmanager.feature.streak

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar

@Composable
fun StreakScreen(
    viewModel: StreakViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Продуктивность",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        StreakContent(
            modifier = Modifier.padding(innerPadding),
            currentStreak = uiState.currentStreak,
            maxStreak = uiState.maxStreak,
            maxStreakPeriod = uiState.maxStreakPeriod,
            minTasksPerDay = uiState.minTasksPerDay,
            restDays = uiState.restDays,
            weeklyTaskCounts = uiState.weeklyTaskCounts,
            onSetMinTasksPerDay = viewModel::setMinTasksPerDay,
            onToggleRestDay = viewModel::toggleRestDay
        )
    }
}