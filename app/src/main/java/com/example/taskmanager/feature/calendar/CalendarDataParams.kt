package com.example.taskmanager.feature.calendar

import java.time.LocalDate
import java.time.YearMonth

data class CalendarDataParams(
    val selectedDate: LocalDate,
    val displayedMonth: YearMonth,
    val displayedWeekStart: LocalDate,
    val viewMode: CalendarViewMode
)

