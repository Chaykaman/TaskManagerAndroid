package com.example.taskmanager.feature.habits.habitlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.habits.habitlist.components.HabitHeader
import com.example.taskmanager.feature.habits.habitlist.components.HabitList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HabitsContent(
    innerPadding: PaddingValues,
    habits: List<HabitListItem>,
    selectedDate: LocalDate,
    completedHabits: Int,
    totalHabits: Int,
    onHabitClick: (Int) -> Unit,
    onHabitDone: (Int) -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("EEEE, d MMMM") }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        HabitHeader(
            completedHabits = completedHabits,
            totalHabits = totalHabits
        )

        Text(
            text = selectedDate
                .format(formatter)
                .replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp)
        )

        HabitList(
            habits = habits,
            onHabitClick = onHabitClick,
            onHabitDone = onHabitDone
        )
    }
}