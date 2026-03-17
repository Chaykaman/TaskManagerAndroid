package com.example.taskmanager.feature.tasklist

import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.taskmanager.feature.tasklist.components.TaskListTopAppBar
import com.example.taskmanager.feature.tasksdisplay.TasksDisplayScreen
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
    var showSortingDisplay by remember { mutableStateOf(false) }
    var showGroupingDisplay by remember { mutableStateOf(false) }

    ScreenScaffold(
        topBar = {
            TaskListTopAppBar(
                onGroupingClick = { showGroupingDisplay = true },
                onSortingClick = { showSortingDisplay = true }
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

        TasksDisplayScreen(
            viewModel = viewModel,
            activeGrouping = uiState.activeGrouping,
            activeSorting = uiState.activeSorting,
            showGroupingSheet = showGroupingDisplay,
            showSortingSheet = showSortingDisplay,
            onGroupingClick = { showGroupingDisplay = it },
            onSortingClick = { showSortingDisplay = it }
        )
    }
}