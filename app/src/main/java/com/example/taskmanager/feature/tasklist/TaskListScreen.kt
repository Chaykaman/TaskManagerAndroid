package com.example.taskmanager.feature.tasklist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.feature.ScreenScaffold
import com.example.taskmanager.feature.tasklist.components.FloatingAddButton
import com.example.taskmanager.feature.tasklist.components.TaskDisplayContent
import com.example.taskmanager.feature.tasklist.components.TaskListTopAppBar
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onTaskClick: (Int) -> Unit,
    onAddTaskClick: (LocalDate?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    ScreenScaffold(
        topBar = {
            TaskListTopAppBar(
                onActionClick = { showBottomSheet = true }
            )
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = { onAddTaskClick(null) }
            )
        }
    ) { innerPadding ->
        TaskListContent(
            innerPadding = innerPadding,
            uiState = uiState,
            onClick = onTaskClick,
            onToggleDone = { task ->
                viewModel.viewModelScope.launch {
                    viewModel.toggleTaskCompletion(task)
                }
            },
            onRemove = { taskId ->
                viewModel.viewModelScope.launch {
                    viewModel.deleteTask(taskId)
                }
            },
            onFilterSelected = { filter -> viewModel.setFilter(filter) }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                TaskDisplayContent(
                    activeSort = uiState.activeSort,
                    onSortField = {
                        sortField -> viewModel.onSortFieldSelected(sortField)
                    }
                )
            }
        }
    }
}