package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.Priority

@Composable
fun TaskPrioritySheet(
    priority: Priority,
    onVisibleChange: () -> Unit,
    onPriorityChange: (Priority) -> Unit
) {
    Column(modifier = Modifier.selectableGroup()) {
        Priority.entries.forEach { priorityItem ->
            val isSelected = priorityItem == priority

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onPriorityChange(priorityItem)
                            onVisibleChange()
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconField(
                    icon = if (isSelected) Icons.Filled.Flag else Icons.Outlined.Flag,
                    tint = priorityItem.color
                )

                Text(
                    text = priorityItem.label,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )

                if (isSelected) {
                    IconField(icon = Icons.Default.Check)
                }
            }
        }
    }
}