package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    placeholderText: String = "Список пуст",
    onClick: (Int) -> Unit,
    onToggleDone: (Task) -> Unit,
    onRemove: (Int) -> Unit
) {
    AnimatedContent(
        targetState = tasks.isEmpty(),
        label = "TaskList"
    ) { isEmpty ->
        if (isEmpty) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = placeholderText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = tasks,
                    key = { task -> task.id }
                ) { task ->
                    SlidingTaskCard(
                        task = task,
                        onClick = onClick,
                        onToggleDone = onToggleDone,
                        onRemove = onRemove,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}