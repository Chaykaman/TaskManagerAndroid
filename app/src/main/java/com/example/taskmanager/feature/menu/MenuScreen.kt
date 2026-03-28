package com.example.taskmanager.feature.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.menu.components.MenuHeader
import com.example.taskmanager.feature.menu.components.MenuRowButton
import com.example.taskmanager.feature.menu.components.MenuSectionHeader
import com.example.taskmanager.feature.taskdetail.components.IconField
import com.example.taskmanager.feature.taskdetail.components.RowField

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
            MenuHeader(
                onNotificationsClick = {},
                onSettingsClick = onAppSettingsClick,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            MenuSectionHeader(title = "Аналитика")

            RowField(modifier = Modifier.clickable(onClick = onSurveyClick)) {
                IconField(
                    icon = Icons.Default.Quiz,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Опрос дня",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            RowField(modifier = Modifier.clickable(onClick = onStatisticsClick)) {
                IconField(
                    icon = Icons.Default.Assessment,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Статистика",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            MenuSectionHeader(title = "Продуктивность")

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

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}