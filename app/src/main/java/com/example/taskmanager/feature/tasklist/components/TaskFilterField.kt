package com.example.taskmanager.feature.tasklist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.TaskFilter
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun TaskFilterField(
    activeFiler: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = activeFiler == TaskFilter.ALL,
                onClick = { onFilterSelected(TaskFilter.ALL) },
                label = { Text("Все") }
            )
        }
        item {
            FilterChip(
                selected = activeFiler == TaskFilter.ACTIVE,
                onClick = { onFilterSelected(TaskFilter.ACTIVE) },
                label = { Text("Активные") }
            )
        }
        item {
            FilterChip(
                selected = activeFiler == TaskFilter.COMPLETED,
                onClick = { onFilterSelected(TaskFilter.COMPLETED) },
                label = { Text("Выполненные") }
            )
        }
        item {
            FilterChip(
                selected = activeFiler == TaskFilter.OVERDUE,
                onClick = { onFilterSelected(TaskFilter.OVERDUE) },
                label = { Text("Просроченные") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskFilterFieldPreview() {
    TaskManagerTheme {
        TaskFilterField(
            activeFiler = TaskFilter.ALL,
            onFilterSelected = {}
        )
    }
}