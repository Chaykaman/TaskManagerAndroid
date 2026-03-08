package com.example.taskmanager.feature.calendar.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.taskmanager.feature.calendar.CalendarViewMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopAppBar(
    calendarMode: CalendarViewMode,
    onCalendarModeChange: (CalendarViewMode) -> Unit,
    onTodayClick: () -> Unit,
) {
    val modeIcon = when (calendarMode) {
        CalendarViewMode.WEEK -> Icons.Default.CalendarViewMonth
        CalendarViewMode.MONTH -> Icons.Default.CalendarViewWeek
    }

    TopAppBar(
        title = {
            Text(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                text = "Календарь"
            )
        },
        actions = {
            IconButton(onClick = onTodayClick) {
                Icon(
                    imageVector = Icons.Default.Today,
                    contentDescription = "Сегодня"
                )
            }
            IconButton(onClick = { onCalendarModeChange(calendarMode.switch()) }) {
                Icon(
                    imageVector = modeIcon,
                    contentDescription = "Вид"
                )
            }
        }
    )
}