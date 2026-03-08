package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    dueDate: LocalDate?,
    onDateSelect: (LocalDate?) -> Unit,
    onShowDatePickerChange: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDate = dueDate,
        yearRange = LocalDate.now().year  ..LocalDate.now().year + 5,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                return date >= LocalDate.now()
            }
        },
    )

    DatePickerDialog(
        onDismissRequest = onShowDatePickerChange,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        onDateSelect(
                            Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        )
                        onShowDatePickerChange()
                    }

                }
            ) { Text("Выбрать") }
        },
        dismissButton = {
            TextButton(onClick = onShowDatePickerChange) { Text("Отмена") }
        }
    ) {
        Column {
            Text(
                "Выбрать дату",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 24.dp, top = 16.dp)
            )

            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false
            )

            if (dueDate != null) {
                TaskCancelDateTime(
                    onCancel = {
                        onDateSelect(null)
                        onShowDatePickerChange()
                    },
                    text = "Сбросить дату"
                )
            }
        }
    }
}