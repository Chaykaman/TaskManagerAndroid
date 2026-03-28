package com.example.taskmanager.feature.habits.habitform.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.habit.HabitFrequency
import com.example.taskmanager.feature.common.SectionHeader
import java.time.DayOfWeek

@Composable
fun HabitFrequencyField(
    selectedFrequency: HabitFrequency,
    selectedDays: Set<DayOfWeek>,
    onFrequencyChanged: (HabitFrequency) -> Unit,
    onDayClick: (DayOfWeek) -> Unit
) {
    SectionHeader(title = "Дни привычек")

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Выбор дней")

        Switch(
            checked = selectedFrequency == HabitFrequency.SPECIFIC_DAYS,
            onCheckedChange = {
                onFrequencyChanged(
                    if (it) HabitFrequency.SPECIFIC_DAYS else HabitFrequency.DAILY
                )
            }
        )
    }

    AnimatedContent(
        targetState = selectedFrequency
    ) { frequency ->
        if (frequency == HabitFrequency.SPECIFIC_DAYS) {
            DaysOfWeekSelector(
                selectedDays = selectedDays,
                onDayClick = onDayClick
            )
        }
    }

    Text(
        text = when (selectedFrequency) {
            HabitFrequency.DAILY -> "Привычка будет доступна каждый день без ограничений"
            HabitFrequency.SPECIFIC_DAYS -> "Выберите конкретные дни недели, в которые будет появляться привычка"
        },
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}