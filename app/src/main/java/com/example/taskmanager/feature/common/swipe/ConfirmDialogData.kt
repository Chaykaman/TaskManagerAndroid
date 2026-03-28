package com.example.taskmanager.feature.common.swipe

data class ConfirmDialogData(
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit,
    val action: SwipeAction
)
