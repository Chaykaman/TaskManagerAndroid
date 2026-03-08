package com.example.taskmanager.feature.tasklist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.ui.theme.TaskManagerTheme
import java.time.LocalDate

@Composable
fun TaskCard(
    task: Task,
    onClick: (Int) -> Unit,
    onToggleDone: (Task) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick(task.id) }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onToggleDone(task) }
            ) {
                AnimatedDoneButton(
                    isCompleted = task.isCompleted,
                    color = task.priority.color
                )
            }

            TaskCardTextFields(
                title = task.title,
                description = task.description,
                dueDate = task.dueDate,
                formattedDueDate = task.formattedDueDate().toString(),
                isCompleted = task.isCompleted,
                isOverdue = task.isOverdue()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskCardPreview() {
    TaskManagerTheme {
        TaskCard(
            task = Task(
                title = "Заголовок задачи",
                description = "Описание задачи",
                isCompleted = false,
                dueDate = LocalDate.now(),
            ),
            onClick = {},
            onToggleDone = {}
        )
    }
}