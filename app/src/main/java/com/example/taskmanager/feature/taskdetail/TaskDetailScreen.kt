package com.example.taskmanager.feature.taskdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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
        onTitleChanged = { newTitle -> viewModel.onTitleChanged(newTitle = newTitle) },
        onDescriptionChanged = { newDescription -> viewModel.onDescriptionChanged(newDescription = newDescription) },
        onToggleIsCompleted = { isCompleted -> viewModel.onCompletedChanged(isCompleted = isCompleted) },
        onPriorityChange = { newPriority -> viewModel.onPriorityChanged(priority = newPriority) },
        onDateSelected = { newDueDate -> viewModel.onDateSelected(date = newDueDate) },
        onTimeSelected = { newDueTime -> viewModel.onTimeSelected(time = newDueTime) },
        onSaveClick = {
            viewModel.viewModelScope.launch {
                viewModel.saveChanges()
            }
            onBack()
        },
        onCancelClick = {
            viewModel.discardChanges()
            onBack()
        },
    )
}