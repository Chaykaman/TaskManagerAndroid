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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun TaskCard(
    title: String,
    description: String,
    isCompleted: Boolean,
    dueDate: LocalDate?,
    formattedDueDate: String,
    isOverdue: Boolean,
    color: Color,
    onClick: () -> Unit,
    onToggleDone: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
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
                onClick = onToggleDone
            ) {
                AnimatedDoneButton(
                    isCompleted = isCompleted,
                    color = color
                )
            }

            TaskCardTextFields(
                title = title,
                description = description,
                dueDate = dueDate,
                formattedDueDate = formattedDueDate,
                isCompleted = isCompleted,
                isOverdue = isOverdue
            )
        }
    }
}