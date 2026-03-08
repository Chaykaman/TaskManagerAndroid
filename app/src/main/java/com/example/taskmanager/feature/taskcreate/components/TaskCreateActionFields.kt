package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.ui.theme.TaskManagerTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskCreateActionFields(
    enabled: Boolean,
    priority: Priority,
    dueDate: LocalDate?,
    dueTime: LocalTime?,
    onPriorityChange: (Priority) -> Unit,
    onDateSelect: (LocalDate?) -> Unit,
    onTimeSelect: (LocalTime?) -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.Companion.weight(1f)
        ) {
            item {
                TaskCreatePriorityField(
                    priority = priority,
                    onPriorityChange = onPriorityChange
                )
            }
            item {
                TaskCreateDateField(
                    dueDate = dueDate,
                    onDateSelect = onDateSelect
                )
            }
            item {
                TaskCreateTimeField(
                    dueTime = dueTime,
                    onTimeSelect = onTimeSelect
                )
            }
        }

        TaskSendButton(
            enabled = enabled,
            onSubmit = onSubmit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskCreateActionFieldsPreview() {
    TaskManagerTheme {
        TaskCreateActionFields(
            enabled = true,
            priority = Priority.PRIORITY_4,
            dueDate = LocalDate.now(),
            dueTime = LocalTime.now(),
            onPriorityChange = {},
            onDateSelect = {},
            onTimeSelect = {},
            onSubmit = {}
        )
    }
}