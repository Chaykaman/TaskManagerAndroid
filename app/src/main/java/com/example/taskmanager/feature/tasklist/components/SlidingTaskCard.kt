package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Task
import kotlinx.coroutines.launch

@Composable
fun SlidingTaskCard(
    task: Task,
    onClick: (Int) -> Unit,
    onToggleDone: (Task) -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val swipeState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()
    var openAlertDialog by remember { mutableStateOf(false) }

    val colorBackground by animateColorAsState(
        when (swipeState.targetValue) {
            SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.outline
            SwipeToDismissBoxValue.StartToEnd -> Color(0xFF32A852)
            SwipeToDismissBoxValue.EndToStart -> Color(0xFFA83232)
        }
    )

    val colorIcon by animateColorAsState(
        when (swipeState.targetValue) {
            SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.onSurface
            SwipeToDismissBoxValue.StartToEnd -> Color.White
            SwipeToDismissBoxValue.EndToStart -> Color.White
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        modifier = modifier.fillMaxWidth(),
        onDismiss = { direction ->
            when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    scope.launch {
                        swipeState.reset()
                        onToggleDone(task)
                    }
                }
                SwipeToDismissBoxValue.EndToStart -> openAlertDialog = true
                SwipeToDismissBoxValue.Settled -> {}
            }
        },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(colorBackground)
                    .padding(horizontal = 20.dp)
            ) {
                when (swipeState.dismissDirection) {
                    SwipeToDismissBoxValue.StartToEnd -> {
                        Icon(
                            imageVector = Icons.Default.TaskAlt,
                            contentDescription = null,
                            tint = colorIcon,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                    SwipeToDismissBoxValue.EndToStart -> {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = colorIcon,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                    SwipeToDismissBoxValue.Settled -> {}
                }
            }
        }
    ) {
        TaskCard(
            task = task,
            onClick = onClick,
            onToggleDone = onToggleDone
        )
    }

    if (openAlertDialog) {
        ConfirmSwipeToDeleteDialog(
            onConfirm = {
                openAlertDialog = false
                onRemove(task.id)
            },
            onDismiss = {
                openAlertDialog = false
                scope.launch {
                    swipeState.reset()
                }
            }
        )
    }
}