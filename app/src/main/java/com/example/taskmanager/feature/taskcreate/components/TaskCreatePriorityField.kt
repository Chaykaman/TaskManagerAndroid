package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties
import com.example.taskmanager.data.local.entity.Priority

@Composable
fun TaskCreatePriorityField(
    priority: Priority,
    onPriorityChange: (Priority) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        AssistChip(
            onClick = { expanded = true },
            label = {
                Text(text = priority.shortLabel)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = priority.label,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = priority.color,
                leadingIconContentColor = priority.color
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false)
        ) {
            Priority.entries.forEach { priorityItem ->
                DropdownMenuItem(
                    onClick = {
                        onPriorityChange(priorityItem)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Приоритет",
                            tint = priorityItem.color
                        )
                    },
                    trailingIcon = {
                        if (priorityItem == priority) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Выбран",
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    },
                    enabled = priorityItem != priority,
                    text = { Text(priorityItem.label) }
                )
            }
        }
    }
}