package com.example.taskmanager.feature.tasksdisplay

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.feature.tasklist.TaskListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksDisplayScreen(
    viewModel: TaskListViewModel,
    activeGrouping: TaskGrouping,
    activeSorting: TaskSorting,
    showGroupingSheet: Boolean = false,
    showSortingSheet: Boolean = false,
    onGroupingClick: (Boolean) -> Unit = {},
    onSortingClick: (Boolean) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()

    if (showGroupingSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onGroupingClick(false) }
        ) {
            GroupingDisplayContent(
                activeGrouping = activeGrouping,
                onGroupingField = { groupingField ->
                    viewModel.setGrouping(groupingField)
                }
            )
        }
    }

    if (showSortingSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onSortingClick(false) }
        ) {
            SortingDisplayContent(
                activeSorting = activeSorting,
                onSortingField = { sortingField ->
                    viewModel.setSorting(sortingField)
                }
            )
        }
    }
}