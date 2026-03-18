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
import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun TaskFilterField(
    activeFilter: TaskFiltering,
    onFilterSelected: (TaskFiltering) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = activeFilter == TaskFiltering.ALL,
                onClick = { onFilterSelected(TaskFiltering.ALL) },
                label = { Text("Все") }
            )
        }
        item {
            FilterChip(
                selected = activeFilter == TaskFiltering.ACTIVE,
                onClick = { onFilterSelected(TaskFiltering.ACTIVE) },
                label = { Text("Активные") }
            )
        }
        item {
            FilterChip(
                selected = activeFilter == TaskFiltering.COMPLETED,
                onClick = { onFilterSelected(TaskFiltering.COMPLETED) },
                label = { Text("Выполненные") }
            )
        }
        item {
            FilterChip(
                selected = activeFilter == TaskFiltering.OVERDUE,
                onClick = { onFilterSelected(TaskFiltering.OVERDUE) },
                label = { Text("Просроченные") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskFilteringFieldPreview() {
    TaskManagerTheme {
        TaskFilterField(
            activeFilter = TaskFiltering.ALL,
            onFilterSelected = {}
        )
    }
}