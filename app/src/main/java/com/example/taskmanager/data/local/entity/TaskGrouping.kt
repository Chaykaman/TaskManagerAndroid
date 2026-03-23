package com.example.taskmanager.data.local.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

enum class TaskGrouping(
    val icon: ImageVector,
    val label: String
) {
    NONE(
        icon = Icons.Default.Block,
        label = "Без группировки"
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