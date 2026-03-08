package com.example.taskmanager.feature.tasklist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.SortField
import com.example.taskmanager.data.local.entity.TaskSort

@Composable
fun TaskDisplayContent(
    activeSort: TaskSort,
    onSortField: (SortField) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Сортировка",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        TaskSortOptions(
            activeSort = activeSort,
            onSortSelected = onSortField
        )
    }
}