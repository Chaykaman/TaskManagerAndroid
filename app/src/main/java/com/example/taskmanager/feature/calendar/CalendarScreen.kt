package com.example.taskmanager.feature.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.calendar.components.CalendarTopAppBar
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.toFabPosition
import com.example.taskmanager.feature.tasklist.components.FloatingAddButton
import com.example.taskmanager.feature.common.LocalFabAlignment
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onTaskClick: (Int) -> Unit,
    onAddTaskClick: (LocalDate) -> Unit,
) {
    val fabAlignment = LocalFabAlignment.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember(currentDate) { currentMonth.minusMonths(60) }
    val endMonth = remember(currentDate) { currentMonth.plusMonths(60) }

    val monthState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = uiState.displayedMonth,
        firstDayOfWeek = DayOfWeek.MONDAY,
        outDateStyle = OutDateStyle.EndOfGrid
    )

    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = uiState.displayedWeekStart,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    ScreenScaffold(
        topBar = {
            CalendarTopAppBar(
                calendarMode = uiState.viewMode,
                onCalendarModeChange = viewModel::onViewModeChanged,
                onTodayClick = { viewModel.onDateSelected(LocalDate.now()) }
            )
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = { onAddTaskClick(uiState.selectedDate) }
            )
        },
        floatingActionButtonPosition = fabAlignment.toFabPosition()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CalendarContent(
                calendarUiState = uiState,
                calendarMode = uiState.viewMode,
                monthState = monthState,
                weekState = weekState,
                currentDate = currentDate,
                onDateSelected = viewModel::onDateSelected,
                onPreviousClick = {
                    when (uiState.viewMode) {
                        CalendarViewMode.MONTH ->
                            viewModel.onMonthChanged(uiState.displayedMonth.minusMonths(1))
                        CalendarViewMode.WEEK ->
                            viewModel.onWeekChanged(uiState.displayedWeekStart.minusWeeks(1))
                    }
                },
                onNextClick = {
                    when (uiState.viewMode) {
                        CalendarViewMode.MONTH ->
                            viewModel.onMonthChanged(uiState.displayedMonth.plusMonths(1))
                        CalendarViewMode.WEEK ->
                            viewModel.onWeekChanged(uiState.displayedWeekStart.plusWeeks(1))
                    }
                },
                onTaskClick = onTaskClick,
                onToggleDone = viewModel::toggleTaskCompletion,
                onRemove = viewModel::deleteTask
            )
        }
    }
}