package com.example.taskmanager.feature.calendar

import com.example.taskmanager.data.local.entity.Task
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val displayedMonth: YearMonth = YearMonth.now(),
    val displayedWeekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY),
    val selectedDate: LocalDate = LocalDate.now(),
    val tasksForSelectedDate: List<Task> = emptyList(),
    val daysWithTasks: Set<LocalDate> = emptySet(),
    val viewMode: CalendarViewMode = CalendarViewMode.WEEK,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
