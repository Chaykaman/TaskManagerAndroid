package com.example.taskmanager.feature.achievements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction

@Composable
fun AchievementsScreen(
    viewModel: AchievementsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var openAlertDialog by remember { mutableStateOf(false) }

    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Достижения",
                onBack = onBack,
                actions = listOf(
                    TopAppAction(
                        icon = Icons.Rounded.SettingsBackupRestore,
                        contentDescription = "Сбросить прогресс",
                        onClick = { openAlertDialog = true }
                    )
                )
            )
        }
    ) { innerPadding ->
        AchievementContent(
            unlockedCount = uiState.unlockedCount,
            totalCount = uiState.totalCount,
            unlockedAchievements = uiState.unlockedAchievements,
            lockedAchievements = uiState.lockedAchievements,
            modifier = Modifier.padding(innerPadding)
        )

        if (openAlertDialog) {
            AlertDialog(
                onDismissRequest = { openAlertDialog = false },
                title = { Text("Сбросить достижения?") },
                text = {
                    Text(
                        text = "Весь прогресс по достижениям будет удалён. Это действие нельзя отменить.",
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.resetAllAchievements()
                            openAlertDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) { Text("Сбросить") }
                },
                dismissButton = {
                    TextButton(onClick = { openAlertDialog = false }) { Text("Отмена") }
                }
            )
        }
    }
}