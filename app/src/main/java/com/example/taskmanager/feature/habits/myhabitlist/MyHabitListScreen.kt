package com.example.taskmanager.feature.habits.myhabitlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.LocalFabAlignment
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.toFabPosition
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction
import com.example.taskmanager.feature.tasklist.components.FloatingAddButton

@Composable
fun MyHabitsScreen(
    viewModel: MyHabitListViewModel = hiltViewModel(),
    onAddHabitClick: () -> Unit,
    onHabitClick: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fabAlignment = LocalFabAlignment.current

    var openHelpDialog by remember { mutableStateOf(false) }

    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Мои привычки",
                onBack = onBack,
                actions = listOf(
                    TopAppAction(
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        contentDescription = "Помощь",
                        onClick = { openHelpDialog = true }
                    )
                )
            )
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = onAddHabitClick
            )
        },
        floatingActionButtonPosition = fabAlignment.toFabPosition()
    ) { innerPadding ->
        MyHabitListContent(
            innerPadding = innerPadding,
            selectedTab = uiState.activeTab,
            onTabSelected = viewModel::onTabSelected,
            habits = uiState.currentHabitList,
            onHabitClick = onHabitClick,
            onToggleArchiveHabit = viewModel::toggleArchive,
            onRemoveHabit = viewModel::deleteHabit
        )

        if (openHelpDialog) {
            AlertDialog(
                title = { Text(text = "Мои привычки") },
                text = {
                    Text(
                        text = "На этой странице вы можете управлять своими привычками. " +
                                "Проведите вправо по карточке, чтобы архивировать привычку, " +
                                "или влево — чтобы удалить."
                    )
                },
                onDismissRequest = { openHelpDialog = false },
                confirmButton = { },
                dismissButton = {
                    TextButton(
                        onClick = { openHelpDialog = false },
                        content = { Text("Понятно")}
                    )
                }
            )
        }
    }
}