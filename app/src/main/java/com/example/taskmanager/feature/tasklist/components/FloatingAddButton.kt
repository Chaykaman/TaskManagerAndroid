package com.example.taskmanager.feature.tasklist.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingAddButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить"
            )
        }
    )
}
