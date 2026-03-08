package com.example.taskmanager.feature.tasklist.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmSwipeToDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = "Удалить задачу?") },
        text = {
            Text(text = "Задача будет удалена навсегда без возможности восстановления.")
        },
        onDismissRequest = { onDismiss },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                content = { Text("Удалить")}
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = { Text("Отмена")}
            )
        }
    )
}