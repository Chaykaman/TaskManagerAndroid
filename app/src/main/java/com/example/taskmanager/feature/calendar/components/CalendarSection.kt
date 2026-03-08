package com.example.taskmanager.feature.calendar.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.taskmanager.feature.calendar.CalendarUiState
import com.example.taskmanager.feature.calendar.CalendarViewMode
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDayPosition
import java.time.LocalDate

@Composable
fun CalendarSection(
    calendarUiState: CalendarUiState,
    calendarMode: CalendarViewMode,
    monthState: CalendarState,
    weekState: WeekCalendarState,
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Column {
        CalendarHeader(
            calendarUiState = calendarUiState,
            currentDate = currentDate,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick
        )

        AnimatedContent(
            targetState = calendarMode,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            modifier = Modifier.graphicsLayer { clip = true }
        ) { calendarType ->
            if (calendarType == CalendarViewMode.WEEK) {
                WeekCalendar(
                    state = weekState,
                    userScrollEnabled = false,
                    weekHeader = { DaysOfWeekRow() },
                    dayContent = { weekDay ->
                        val isSelectable = weekDay.position == WeekDayPosition.RangeDate
                        val day = weekDay.date

                        CalendarDayCell(
                            day = day,
                            today = currentDate,
                            isSelected = day == calendarUiState.selectedDate,
                            isSelectable = isSelectable,
                            hasTask = day in calendarUiState.daysWithTasks,
                            onDateSelected = onDateSelected
                        )
                    }
                )
            } else {
                HorizontalCalendar(
                    state = monthState,
                    userScrollEnabled = false,
                    monthHeader = { DaysOfWeekRow() },
                    dayContent = { monthDay ->
                        val isSelectable = monthDay.position == DayPosition.MonthDate
                        val day = monthDay.date

                        CalendarDayCell(
                            day = day,
                            today = currentDate,
                            isSelected = day == calendarUiState.selectedDate,
                            isSelectable = isSelectable,
                            hasTask = day in calendarUiState.daysWithTasks,
                            onDateSelected = onDateSelected
                        )
                    }
                )
            }
        }
    }
}