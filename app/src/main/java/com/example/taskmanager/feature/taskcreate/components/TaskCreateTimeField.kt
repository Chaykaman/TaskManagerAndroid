package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.taskmanager.feature.taskdetail.components.ChipField
import com.example.taskmanager.feature.taskdetail.components.TaskTimePicker
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskCreateTimeField(
    dueTime: LocalTime?,
    onTimeSelect: (LocalTime?) -> Unit,
) {
    var showTimePicker by remember { mutableStateOf(false) }

    ChipField(
        onClick = { showTimePicker = true },
        label = {
            Text(
                text = dueTime?.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                ) ?: "Время"
            )
        },
        icon = Icons.Default.AccessTime,
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