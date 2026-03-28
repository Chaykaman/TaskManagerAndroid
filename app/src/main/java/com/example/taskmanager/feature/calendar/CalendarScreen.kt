package com.example.taskmanager.feature.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.LocalFabAlignment
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.toFabPosition
import com.example.taskmanager.feature.common.topappbar.ScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction
import com.example.taskmanager.feature.tasklist.components.FloatingAddButton
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

    val modeIcon = when (uiState.viewMode) {
        CalendarViewMode.WEEK -> Icons.Rounded.CalendarViewMonth
        CalendarViewMode.MONTH -> Icons.Rounded.CalendarViewWeek
    }

    ScreenScaffold(
        topBar = {
            ScreenTopAppBar(
                title = "Календарь",
                actions = listOf(
                    TopAppAction(
                        icon = Icons.Rounded.Today,
                        contentDescription = "Сегодня",
                        onClick = { viewModel.onDateSelected(LocalDate.now()) }
                    ),
                    TopAppAction(
                        icon = modeIcon,
                        contentDescription = "Вид",
                        onClick = { viewModel.onViewModeChanged(uiState.viewMode.switch()) }
                    )
                )
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