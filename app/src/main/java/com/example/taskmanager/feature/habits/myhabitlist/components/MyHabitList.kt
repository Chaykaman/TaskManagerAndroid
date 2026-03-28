package com.example.taskmanager.feature.habits.myhabitlist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.feature.common.ConfirmSwipeToDeleteDialog
import com.example.taskmanager.feature.common.swipe.SlidingCard
import com.example.taskmanager.feature.common.swipe.SwipeAction

@Composable
fun MyHabitList(
    habits: List<Habit>,
    onHabitClick: (Int) -> Unit,
    onToggleArchive: (Habit) -> Unit,
    onRemove: (Habit) -> Unit
) {
    AnimatedContent(
        targetState = habits.isEmpty(),
        label = "HabitList"
    ) { isEmpty ->
        if (isEmpty) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = "Список пуст",
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
                    top = 0.dp,
                    bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = habits,
                    key = { habit -> habit.id },
                ) { habit ->
                    SlidingCard(
                        modifier = Modifier.animateItem(),
                        startToEndAction = SwipeAction(
                            icon = Icons.Outlined.Archive,
                            backgroundColor = Color(0xFFFCBA03),
                            onSwipe = { onToggleArchive(habit) }
                        ),
                        endToStartAction = SwipeAction(
                            icon = Icons.Outlined.DeleteOutline,
                            backgroundColor = Color(0xFFA83232),
                            requiresConfirmation = true,
                            onSwipe = { onRemove(habit) }
                        ),
                        confirmDialog = { data ->
                            ConfirmSwipeToDeleteDialog(
                                title = "Удалить привычку?",
                                description = "Привычка будет удалена навсегда без возможности восстановления.",
                                onConfirm = data.onConfirm,
                                onDismiss = data.onDismiss
                            )
                        }
                    ) {
                        MyHabitCard(
                            title = habit.title,
                            iconName = habit.iconName,
                            color = habit.color,
                            onClick = { onHabitClick(habit.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}