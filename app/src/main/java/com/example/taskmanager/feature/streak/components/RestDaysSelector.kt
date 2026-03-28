package com.example.taskmanager.feature.streak.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.common.DayChip
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun RestDaysSelector(
    selectedDays: Set<DayOfWeek>,
    onDayToggled: (DayOfWeek) -> Unit
) {
    Column {
        Text(
            text = "Пропуск в эти дни не влияет на серию",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayOfWeek.entries.forEach { day ->
                val isSelected = day in selectedDays
                DayChip(
                    dayName = day.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                        .replaceFirstChar { it.uppercase() },
                    isSelected = isSelected,
                    onClick = { onDayToggled(day) }
                )
            }
        }
    }
}