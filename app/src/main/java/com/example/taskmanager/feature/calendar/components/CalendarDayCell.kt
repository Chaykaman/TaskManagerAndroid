package com.example.taskmanager.feature.calendar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate

private val WeekendDays = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

@Composable
fun CalendarDayCell(
    day: LocalDate,
    today: LocalDate,
    isSelected: Boolean,
    isSelectable: Boolean,
    hasTask: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val onClick = remember(day, onDateSelected) { { onDateSelected(day) } }

    val colorScheme = MaterialTheme.colorScheme
    val isWeekend = day.dayOfWeek in WeekendDays
    val isToday = day == today

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(6.dp)
                .clip(CircleShape)
                .drawBehind {
                    if (isSelected) {
                        drawCircle(color = colorScheme.primary)
                    }

                    if (isToday) {
                        drawCircle(
                            color = colorScheme.primary,
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }

                    if (hasTask) {
                        drawCircle(
                            color = colorScheme.primary,
                            radius = 3.dp.toPx(),
                            center = center.copy(y = size.height - 6.dp.toPx())
                        )
                    }
                }
                .clickable(
                    enabled = isSelectable,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 20.dp),
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = when {
                    isSelected -> colorScheme.onPrimary
                    isWeekend && isSelectable -> colorScheme.error
                    !isSelectable -> colorScheme.onSurface.copy(alpha = 0.3f)
                    else -> colorScheme.onSurface
                }
            )
        }
    }
}