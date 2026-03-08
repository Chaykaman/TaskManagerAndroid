package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskTimeField(
    dueTime: LocalTime?,
    onTimeSelect: (LocalTime?) -> Unit,
) {
    var showTimePicker by remember { mutableStateOf(false) }

    RowField {
        IconField(icon = Icons.Filled.AccessTimeFilled)

        ChipField(
            onClick = { showTimePicker = true },
            label = {
                Text(
                    text = dueTime?.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ) ?: "Выбрать время"
                )
            },
            icon = Icons.Outlined.AccessTime,
            isActive = dueTime != null
        )

        if (showTimePicker) {
            TaskTimePicker(
                dueTime = dueTime,
                onTimeSelect = { onTimeSelect(it) },
                onShowTimePickerChange = { showTimePicker = false }
            )
        }
    }
}