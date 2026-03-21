package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePicker(
    dueTime: LocalTime?,
    onTimeSelect: (LocalTime?) -> Unit,
    onShowTimePickerChange: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = dueTime?.hour ?: (LocalTime.now().hour + 1),
        initialMinute = dueTime?.minute ?: 0
    )

    AlertDialog(
        onDismissRequest = onShowTimePickerChange,
        title = { Text("Выбрать время") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimePicker(state = timePickerState)
                if (dueTime != null) {
                    TaskCancelDateTime(
                        onCancel = {
                            onTimeSelect(null)
                            onShowTimePickerChange()
                        },
                        text = "Сбросить время"
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelect(
                        LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                    )
                    onShowTimePickerChange()
                }
            ) { Text("Выбрать") }
        },
        dismissButton = {
            TextButton(onClick = onShowTimePickerChange) { Text("Отмена") }
        }
    )
}