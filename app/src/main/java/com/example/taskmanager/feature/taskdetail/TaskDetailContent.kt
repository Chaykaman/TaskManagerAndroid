package com.example.taskmanager.feature.taskdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.feature.taskdetail.components.TaskDateField
import com.example.taskmanager.feature.taskdetail.components.TaskDescriptionField
import com.example.taskmanager.feature.taskdetail.components.TaskHeader
import com.example.taskmanager.feature.taskdetail.components.TaskPriorityField
import com.example.taskmanager.feature.taskdetail.components.TaskTimeField
import com.example.taskmanager.feature.taskdetail.components.TaskTitleHeader
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskDetailContent(
    uiState: TaskDetailUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onToggleIsCompleted: (Boolean) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
    onTimeSelected: (LocalTime?) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        TaskHeader(
            enabled = uiState.hasUnsavedChanges && uiState.draftTitle.isNotBlank(),
            onSaveClick = onSaveClick,
            onCancelClick = onCancelClick
        )

        TaskTitleHeader(
            title = uiState.draftTitle,
            isCompleted = uiState.draftIsCompleted,
            color = uiState.draftPriority.color,
            onTitleChange = { newTitle -> onTitleChanged(newTitle) },
            onToggleIsCompleted = { isCompleted -> onToggleIsCompleted(isCompleted) },
        )

        TaskDescriptionField(
            value = uiState.draftDescription,
            onValueChange = { newDescription -> onDescriptionChanged(newDescription) },
        )

        TaskPriorityField(
            priority = uiState.draftPriority,
            onPriorityChange = { newPriority -> onPriorityChange(newPriority) }
        )

        TaskDateField(
            dueDate = uiState.draftDueDate,
            onDateSelect = { newDueDate -> onDateSelected(newDueDate) }
        )

        TaskTimeField(
            dueTime = uiState.draftDueTime,
            onTimeSelect = { newDueTime -> onTimeSelected(newDueTime) }
        )
    }
}