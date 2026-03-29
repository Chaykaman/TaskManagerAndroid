package com.example.taskmanager.feature.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction
import com.example.taskmanager.feature.menu.components.MenuRowButton
import com.example.taskmanager.feature.menu.components.MenuSection

@Composable
fun MenuScreen(
    onSurveyClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onProductivityClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onAppSettingsClick: () -> Unit,
) {
    ScreenScaffold(
        topBar = {
            ScreenTopAppBar(
                title = "Обзор",
                actions = listOf(
                    TopAppAction(
                        icon = Icons.Rounded.Notifications,
                        contentDescription = "Уведомления",
                        onClick = { }
                    ),
                    TopAppAction(
                        icon = Icons.Rounded.Settings,
                        contentDescription = "Настройки",
                        onClick = onAppSettingsClick
                    ),
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            MenuSection(title = "Аналитика") {
                MenuRowButton(
                    onClick = onSurveyClick,
                    icon = Icons.Rounded.Quiz,
                    text = "Опрос дня"
                )

                MenuRowButton(
                    onClick = onStatisticsClick,
                    icon = Icons.Rounded.Assessment,
                    text = "Статистика"
                )
            }

            MenuSection(title = "Продуктивность") {
                MenuRowButton(
                    onClick = onProductivityClick,
                    icon = Icons.Rounded.LocalFireDepartment,
                    text = "Продуктивность"
                )

                MenuRowButton(
                    onClick = onAchievementsClick,
                    icon = Icons.Rounded.EmojiEvents,
                    text = "Достижения"
                )
            }
        }
    }
}