package com.example.taskmanager.data.local.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Today
import androidx.compose.ui.graphics.vector.ImageVector

enum class TaskGrouping(
    val icon: ImageVector,
    val label: String
) {
    NONE(
        icon = Icons.Rounded.Block,
        label = "Без группировки"
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