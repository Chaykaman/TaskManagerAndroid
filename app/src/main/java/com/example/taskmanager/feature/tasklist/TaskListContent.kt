package com.example.taskmanager.feature.tasklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.feature.tasklist.components.TaskFilterField
import com.example.taskmanager.feature.tasklist.components.TaskList

@Composable
fun TaskListContent(
    innerPadding: PaddingValues,
    uiState: TaskListUiState,
    onClick: (Int) -> Unit,
    onToggleDone: (Task) -> Unit,
    onRemove: (Int) -> Unit,
    onFilterSelected: (TaskFiltering) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        TaskFilterField(
            activeFilter = uiState.activeFiltering,
            onFilterSelected = onFilterSelected
        )

        TaskList(
            items = uiState.items,
            placeholderText = when (uiState.activeFiltering) {
                TaskFiltering.COMPLETED -> { "Пока нет выполненных задач.\nСамое время закрыть первую!" }
                TaskFiltering.OVERDUE -> {"Просроченных задач нет.\nТак держать!"}
                else -> {"Задач пока нет.\nНажмите +, чтобы добавить новую."}
            },
            onClick = onClick,
            onToggleDone = onToggleDone,
            onRemove = onRemove,
        )
    }
}