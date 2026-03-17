package com.example.taskmanager.feature.tasklist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

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
