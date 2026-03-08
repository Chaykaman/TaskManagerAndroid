package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.tasklist.components.AnimatedDoneButton

@Composable
fun TaskTitleHeader(
    title: String,
    isCompleted: Boolean,
    color: Color,
    onTitleChange: (String) -> Unit,
    onToggleIsCompleted: (Boolean) -> Unit,
) {
    RowField(modifier = Modifier.padding(bottom = 8.dp)) {
        IconButton(onClick = { onToggleIsCompleted(!isCompleted) }) {
            AnimatedDoneButton(
                isCompleted = isCompleted,
                color = color
            )
        }

        TaskTitleField(
            value = title,
            onValueChange = onTitleChange,
            isCompleted = isCompleted,
        )
    }
}