package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.taskmanager.data.local.entity.Priority
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPriorityField(
    priority: Priority,
    onPriorityChange: (Priority) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    RowField {
        IconField(icon = Icons.Rounded.Flag)

        AssistChip(
            onClick = { showBottomSheet = true },
            label = {
                Text(
                    text = priority.label,
                    color = priority.color,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Flag,
                    contentDescription = "Приоритет",
                    tint = priority.color,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                TaskPrioritySheet(
                    priority = priority,
                    onVisibleChange = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    },
                    onPriorityChange = { onPriorityChange(it) }
                )
            }
        }
    }
}