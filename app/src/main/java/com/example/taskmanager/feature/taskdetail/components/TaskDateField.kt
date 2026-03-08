package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TaskDateField(
    dueDate: LocalDate?,
    onDateSelect: (LocalDate?) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    RowField {
        IconField(icon = Icons.Filled.EditCalendar)

        ChipField(
            onClick = { showDatePicker = true },
            label = {
                Text(
                    text = dueDate?.format(
                        DateTimeFormatter.ofPattern("d MMMM yyyy")
                    ) ?: "Выбрать дату"
                )
            },
            icon = Icons.Filled.CalendarMonth,
            isActive = dueDate != null
        )

        if (showDatePicker) {
            TaskDatePicker(
                dueDate = dueDate,
                onDateSelect = { onDateSelect(it) },
                onShowDatePickerChange = { showDatePicker = false }
            )
        }
    }
}