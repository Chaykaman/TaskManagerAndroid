package com.example.taskmanager.feature.common.swipe

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SwipeAction(
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconTint: Color = Color.White,
    val requiresConfirmation: Boolean = false,
    val onSwipe: () -> Unit
)
