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
import com.example.taskmanager.feature.tasklist.TaskListItem

@Composable
fun TaskList(
    items: List<TaskListItem>,
    placeholderText: String = "Список пуст",
    onClick: (Int) -> Unit,
    onToggleDone: (Task) -> Unit,
    onRemove: (Int) -> Unit
) {
    AnimatedContent(
        targetState = items.isEmpty(),
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
                    items = items,
                    key = { item ->
                        when (item) {
                            is TaskListItem.Header -> "header_${item.title}"
                            is TaskListItem.TaskItem -> "task_${item.task.id}"
                        }
                    },
                    contentType = { item ->
                        when (item) {
                            is TaskListItem.Header -> "header"
                            is TaskListItem.TaskItem -> "task"
                        }
                    }
                ) { item ->
                    when (item) {
                        is TaskListItem.Header -> GroupHeader(text = item.title)
                        is TaskListItem.TaskItem -> SlidingTaskCard(
                            task = item.task,
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
}