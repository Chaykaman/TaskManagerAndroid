package com.example.taskmanager.feature.taskdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = hiltViewModel(),
    taskId: Int,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    TaskDetailContent(
        uiState = uiState,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onToggleIsCompleted = viewModel::onCompletedChanged,
        onPriorityChange = viewModel::onPriorityChanged,
        onDateSelected = viewModel::onDateSelected,
        onTimeSelected = viewModel::onTimeSelected,
        onSaveClick = {
            viewModel.saveChanges()
            onBack()
        },
        onCancelClick = {
            viewModel.discardChanges()
            onBack()
        },
    )
}