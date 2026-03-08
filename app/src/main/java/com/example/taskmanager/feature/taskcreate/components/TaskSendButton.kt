package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TaskSendButton(
    enabled: Boolean,
    onSubmit: () -> Unit
) {
    IconButton(
        onClick = onSubmit,
        enabled = enabled,
        colors = IconButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Отправить"
        )
    }
}