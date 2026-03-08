package com.example.taskmanager.feature.taskcreate

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.feature.taskcreate.components.TaskCreateActionFields
import com.example.taskmanager.feature.taskcreate.components.TaskCreateTextFields
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskCreateContent(
    title: String,
    description: String,
    priority: Priority,
    dueDate: LocalDate?,
    dueTime: LocalTime?,
    focusRequester: FocusRequester,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onDateSelect: (LocalDate?) -> Unit,
    onTimeSelect: (LocalTime?) -> Unit,
    onSubmit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
    ) {
        item {
            TaskCreateTextFields(
                title = title,
                description = description,
                focusRequester = focusRequester,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange,
                onSubmit = onSubmit
            )
        }

        item {
            TaskCreateActionFields(
                enabled = title.isNotBlank(),
                priority = priority,
                dueDate = dueDate,
                dueTime = dueTime,
                onPriorityChange = onPriorityChange,
                onDateSelect = onDateSelect,
                onTimeSelect = onTimeSelect,
                onSubmit = onSubmit
            )
        }
    }
}