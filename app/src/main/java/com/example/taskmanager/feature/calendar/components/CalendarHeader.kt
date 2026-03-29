package com.example.taskmanager.feature.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.calendar.CalendarUiState
import com.example.taskmanager.feature.calendar.CalendarViewMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val SHORT_WEEK_PATTERN = "d MMMM"
private const val FULL_WEEK_PATTERN = "d MMM yyyy"

@Composable
fun CalendarHeader(
    calendarUiState: CalendarUiState,
    currentDate: LocalDate,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val languageTag = remember { Locale.forLanguageTag("ru") }

    val calendarTitle = when (calendarUiState.viewMode) {
        CalendarViewMode.WEEK -> {
            val weekStart = calendarUiState.displayedWeekStart
            val weekEnd = weekStart.plusDays(6)
            val formatter = DateTimeFormatter.ofPattern(
                if (weekEnd.year == currentDate.year) SHORT_WEEK_PATTERN else FULL_WEEK_PATTERN,
                languageTag
            )

            if (weekStart.month == weekEnd.month) {
                "${weekStart.dayOfMonth} — ${weekEnd.format(formatter)}"
            } else {
                "${weekStart.format(formatter)} — ${weekEnd.format(formatter)}"
            }
        }
        CalendarViewMode.MONTH -> {
            val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", languageTag)
            calendarUiState.displayedMonth.format(formatter).replaceFirstChar { it.uppercase() }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Назад"
            )
        }

        Text(
            text = calendarTitle,
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Вперёд"
            )
        }
    }
}