package com.example.taskmanager.feature.tasklist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.outlined.ViewStream
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.toFabPosition
import com.example.taskmanager.feature.tasklist.components.FloatingAddButton
import com.example.taskmanager.feature.tasksdisplay.TasksDisplayScreen
import com.example.taskmanager.feature.common.LocalFabAlignment
import com.example.taskmanager.feature.common.topappbar.ScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onTaskClick: (Int) -> Unit,
    onAddTaskClick: (LocalDate?) -> Unit,
) {
    val fabAlignment = LocalFabAlignment.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSortingDisplay by remember { mutableStateOf(false) }
    var showGroupingDisplay by remember { mutableStateOf(false) }

    ScreenScaffold(
        topBar = {
            ScreenTopAppBar(
                title = "Задачи",
                actions = listOf(
                    TopAppAction(
                        icon = Icons.Outlined.ViewStream,
                        contentDescription = "Группировка",
                        onClick = { showGroupingDisplay = true }
                    ),
                    TopAppAction(
                        icon = Icons.AutoMirrored.Rounded.Sort,
                        contentDescription = "Сортировка",
                        onClick = { showSortingDisplay = true }
                    )
                )
            )
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = { onAddTaskClick(null) }
            )
        },
        floatingActionButtonPosition = fabAlignment.toFabPosition()
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.isLoading,
            label = "TaskListLoading"
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { LoadingIndicator() }
                )
            } else {
                TaskListContent(
                    innerPadding = innerPadding,
                    uiState = uiState,
                    onClick = onTaskClick,
                    onToggleDone = viewModel::toggleTaskCompletion,
                    onRemove = viewModel::deleteTask,
                    onFilterSelected = viewModel::setFilter,
                )
            }

            if (showGroupingDisplay || showSortingDisplay) {
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
    }
}