package com.example.taskmanager.feature.tasksdisplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.SortingField
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.feature.tasksdisplay.components.SortingOptions

@Composable
fun SortingDisplayContent(
    activeSorting: TaskSorting,
    onSortingField: (SortingField) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Сортировка",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        SortingOptions(
            activeSorting = activeSorting,
            onSortingSelected = onSortingField
        )
    }
}