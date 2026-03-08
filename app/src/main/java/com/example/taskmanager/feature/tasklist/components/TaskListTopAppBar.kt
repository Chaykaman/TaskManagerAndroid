package com.example.taskmanager.feature.tasklist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskmanager.ui.theme.TaskManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListTopAppBar(
    onActionClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Задачи",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(
                onClick = onActionClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Sort,
                    contentDescription = "Сортировка"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TaskListTopAppBarPreview() {
    TaskManagerTheme {
        TaskListTopAppBar(
            onActionClick = {}
        )
    }
}