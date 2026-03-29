package com.example.taskmanager.data.local.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Today
import androidx.compose.ui.graphics.vector.ImageVector

enum class SortingField(
    val icon: ImageVector,
    val label: String
) {
    ID(
        icon = Icons.AutoMirrored.Rounded.Notes,
        label = "По умолчанию"
    ),
    TITLE(
        icon = Icons.Rounded.TextFields,
        label = "По названию"
    ),
    DUE_DATE(
        icon = Icons.Rounded.Today,
        label = "По дате выполнения"
    ),
    PRIORITY(
        icon = Icons.Rounded.Flag,
        label = "По приоритету"
    )
}

enum class SortingDirection(
    val icon: ImageVector,
    val label: String
) {
    ASC(
        icon = Icons.Rounded.ArrowUpward,
        label = "По возрастанию"
    ),
    DESC(
        icon = Icons.Rounded.ArrowDownward,
        label = "По убыванию"
    )
}

data class TaskSorting(
    val field: SortingField = SortingField.ID,
    val direction: SortingDirection = SortingDirection.ASC
)
