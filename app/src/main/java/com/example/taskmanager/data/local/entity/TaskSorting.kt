package com.example.taskmanager.data.local.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

enum class SortingField(
    val icon: ImageVector,
    val label: String
) {
    ID(
        icon = Icons.AutoMirrored.Default.Notes,
        label = "По умолчанию"
    ),
    TITLE(
        icon = Icons.Default.TextFields,
        label = "По названию"
    ),
    DUE_DATE(
        icon = Icons.Default.Today,
        label = "По дате выполнения"
    ),
    PRIORITY(
        icon = Icons.Default.Flag,
        label = "По приоритету"
    )
}

enum class SortingDirection(
    val icon: ImageVector,
    val label: String
) {
    ASC(
        icon = Icons.Default.ArrowUpward,
        label = "По возрастанию"
    ),
    DESC(
        icon = Icons.Default.ArrowDownward,
        label = "По убыванию"
    )
}

data class TaskSorting(
    val field: SortingField = SortingField.ID,
    val direction: SortingDirection = SortingDirection.ASC
)
