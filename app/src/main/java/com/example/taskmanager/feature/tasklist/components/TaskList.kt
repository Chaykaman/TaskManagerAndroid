package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.feature.common.ConfirmSwipeToDeleteDialog
import com.example.taskmanager.feature.common.swipe.SlidingCard
import com.example.taskmanager.feature.common.swipe.SwipeAction
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
                contentPadding = PaddingValues(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 80.dp
                ),
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
                        is TaskListItem.TaskItem -> {
                            val task = item.task
                            SlidingCard(
                                modifier = Modifier.animateItem(),
                                startToEndAction = SwipeAction(
                                    icon = Icons.Outlined.DoneAll,
                                    backgroundColor = Color(0xFF32A852),
                                    onSwipe = { onToggleDone(item.task) }
                                ),
                                endToStartAction = SwipeAction(
                                    icon = Icons.Rounded.DeleteOutline,
                                    backgroundColor = Color(0xFFA83232),
                                    requiresConfirmation = true,
                                    onSwipe = { onRemove(item.task.id) }
                                ),
                                confirmDialog = { data ->
                                    ConfirmSwipeToDeleteDialog(
                                        title = "Удалить задачу?",
                                        description = "Задача будет удалена навсегда без возможности восстановления.",
                                        onConfirm = data.onConfirm,
                                        onDismiss = data.onDismiss
                                    )
                                }
                            ) {
                                TaskCard(
                                    title = task.title,
                                    description = task.description,
                                    isCompleted = task.isCompleted,
                                    dueDate = task.dueDate,
                                    formattedDueDate = task.formattedDueDate(),
                                    isOverdue = task.isOverdue(),
                                    color = task.priority.color,
                                    onClick = { onClick(task.id) },
                                    onToggleDone = { onToggleDone(task) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}