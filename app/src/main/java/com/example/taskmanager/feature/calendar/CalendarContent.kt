package com.example.taskmanager.feature.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.feature.calendar.components.CalendarSection
import com.example.taskmanager.feature.tasklist.components.TaskList
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarContent(
    calendarUiState: CalendarUiState,
    calendarMode: CalendarViewMode,
    monthState: CalendarState,
    weekState: WeekCalendarState,
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onToggleDone: (Task) -> Unit,
    onRemove: (Int) -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("EEEE, d MMMM") }

    CalendarSection(
        calendarUiState = calendarUiState,
        calendarMode = calendarMode,
        monthState = monthState,
        weekState = weekState,
        currentDate = currentDate,
        onDateSelected = onDateSelected,
        onPreviousClick = onPreviousClick,
        onNextClick = onNextClick
    )

    Text(
        text = calendarUiState.selectedDate.format(formatter).replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    TaskList(
        tasks = calendarUiState.tasksForSelectedDate,
        placeholderText = when {
            calendarUiState.selectedDate == currentDate -> {"На сегодня задач нет"}
            else -> {"На этот день задач нет"}
        },
        onClick = onTaskClick,
        onToggleDone = onToggleDone,
        onRemove = onRemove
    )
}