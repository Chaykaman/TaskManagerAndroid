package com.example.taskmanager.feature.tasklist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.SortDirection
import com.example.taskmanager.data.local.entity.SortField
import com.example.taskmanager.data.local.entity.TaskSort
import com.example.taskmanager.feature.taskdetail.components.IconField

@Composable
fun TaskSortOptions(
    activeSort: TaskSort,
    onSortSelected: (SortField) -> Unit,
) {
    LazyColumn(modifier = Modifier.selectableGroup()) {
        items(SortField.entries) { currentSortField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = activeSort.field == currentSortField,
                        onClick = { onSortSelected(currentSortField) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconField(
                    icon = currentSortField.icon,
                    tint = if (activeSort.field == currentSortField) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    }
                )

                TaskSortOptionTextFields(
                    title = currentSortField.label,
                    visible = activeSort.field == currentSortField,
                    directionText = activeSort.direction.label,
                )

                Spacer(modifier = Modifier.weight(1f))

                AnimatedDirectionIcon(
                    targetValue = activeSort.direction == SortDirection.ASC,
                    visible = activeSort.field == currentSortField,
                )
            }
        }
    }
}