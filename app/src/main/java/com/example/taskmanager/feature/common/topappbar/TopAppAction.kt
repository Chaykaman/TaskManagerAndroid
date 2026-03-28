package com.example.taskmanager.feature.common.topappbar

import androidx.compose.ui.graphics.vector.ImageVector

data class TopAppAction(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)
