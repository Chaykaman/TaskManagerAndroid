package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.taskmanager.feature.taskdetail.components.ChipField
import com.example.taskmanager.feature.taskdetail.components.TaskDatePicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TaskCreateDateField(
    dueDate: LocalDate?,
    onDateSelect: (LocalDate?) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    ChipField(
        onClick = { showDatePicker = true },
        label = {
            Text(
                text = dueDate?.format(
                    DateTimeFormatter.ofPattern("dd MMM")
                ) ?: "Дата"
            )
        },
        icon = Icons.Default.CalendarToday,
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