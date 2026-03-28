package com.example.taskmanager.feature.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmSwipeToDeleteDialog(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = title) },
        text = {
            Text(text = description)
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