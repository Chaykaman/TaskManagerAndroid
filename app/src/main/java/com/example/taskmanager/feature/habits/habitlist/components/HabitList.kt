package com.example.taskmanager.feature.habits.habitlist.components

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
import com.example.taskmanager.feature.habits.habitlist.HabitListItem
import com.example.taskmanager.feature.habits.myhabitlist.components.HabitCard

@Composable
fun HabitList(
    habits: List<HabitListItem>,
    onHabitClick: (Int) -> Unit,
    onHabitDone: (Int) -> Unit
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
                        text = "Список привычек пуст",
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
                    items = habits,
                    key = { item -> item.habit.id },
                ) { item ->
                    val habit = item.habit

                    HabitCard(
                        title = habit.title,
                        iconName = habit.iconName,
                        color = habit.color,
                        isCompleted = item.isCompletedToday,
                        onClick = { onHabitClick(habit.id) },
                        onDone = { onHabitDone(habit.id) },
                    )
                }
            }
        }
    }
}