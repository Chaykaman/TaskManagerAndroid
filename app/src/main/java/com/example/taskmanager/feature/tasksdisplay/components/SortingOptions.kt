package com.example.taskmanager.feature.tasksdisplay.components

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
import com.example.taskmanager.data.local.entity.SortingDirection
import com.example.taskmanager.data.local.entity.SortingField
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.feature.taskdetail.components.IconField
import com.example.taskmanager.feature.tasklist.components.AnimatedDirectionIcon

@Composable
fun SortingOptions(
    activeSorting: TaskSorting,
    onSortingSelected: (SortingField) -> Unit,
) {
    LazyColumn(modifier = Modifier.selectableGroup()) {
        items(SortingField.entries) { currentSortingField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = activeSorting.field == currentSortingField,
                        onClick = { onSortingSelected(currentSortingField) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconField(
                    icon = currentSortingField.icon,
                    tint = if (activeSorting.field == currentSortingField) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    }
                )

                SortingOptionTextFields(
                    title = currentSortingField.label,
                    visible = activeSorting.field == currentSortingField,
                    directionText = activeSorting.direction.label,
                )

                Spacer(modifier = Modifier.weight(1f))

                AnimatedDirectionIcon(
                    targetValue = activeSorting.direction == SortingDirection.ASC,
                    visible = activeSorting.field == currentSortingField,
                )
            }
        }
    }
}