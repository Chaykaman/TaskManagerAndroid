package com.example.taskmanager.feature.common.swipe

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.launch

@Composable
fun SlidingCard(
    modifier: Modifier = Modifier,
    startToEndAction: SwipeAction? = null,
    endToStartAction: SwipeAction? = null,
    confirmDialog: @Composable (ConfirmDialogData) -> Unit,
    enabledDirections: Set<SwipeToDismissBoxValue> = setOf(
        SwipeToDismissBoxValue.StartToEnd,
        SwipeToDismissBoxValue.EndToStart
    ),
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()

    var pendingAction by remember { mutableStateOf<SwipeAction?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val colorBackground by animateColorAsState(
        when (swipeState.targetValue) {
            SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.outline
            SwipeToDismissBoxValue.StartToEnd -> startToEndAction?.backgroundColor ?: Color.Transparent
            SwipeToDismissBoxValue.EndToStart -> endToStartAction?.backgroundColor ?: Color.Transparent
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
            if (showDialog) return@SwipeToDismissBox

            val action = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> startToEndAction
                SwipeToDismissBoxValue.EndToStart -> endToStartAction
                else -> null
            }

            if (direction !in enabledDirections || action == null) {
                scope.launch {
                    swipeState.reset()
                }
                return@SwipeToDismissBox
            }

            if (action.requiresConfirmation) {
                pendingAction = action
                showDialog = true
            } else {
                scope.launch {
                    swipeState.reset()
                    action.onSwipe()
                }
            }
        },
        backgroundContent = {
            val direction = swipeState.dismissDirection

            val action = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> startToEndAction
                SwipeToDismissBoxValue.EndToStart -> endToStartAction
                else -> null
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(colorBackground)
                    .padding(horizontal = 20.dp)
            ) {
                if (action != null) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null,
                        tint = colorIcon,
                        modifier = Modifier.align(
                            if (direction == SwipeToDismissBoxValue.StartToEnd)
                                Alignment.CenterStart
                            else
                                Alignment.CenterEnd
                        )
                    )
                }
            }
        }
    ) {
        content()
    }

    if (showDialog) {
        pendingAction?.let { action ->
            confirmDialog(
                ConfirmDialogData(
                    onConfirm = {
                        pendingAction = null
                        scope.launch {
                            swipeState.reset()
                            showDialog = false
                            action.onSwipe()
                        }
                    },
                    onDismiss = {
                        pendingAction = null
                        scope.launch {
                            swipeState.reset()
                            showDialog = false
                        }
                    },
                    action = action
                )
            )
        }
    }

}