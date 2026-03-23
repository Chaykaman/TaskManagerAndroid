package com.example.taskmanager.feature.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.menu.components.MenuHeader
import com.example.taskmanager.feature.taskdetail.components.IconField
import com.example.taskmanager.feature.taskdetail.components.RowField

@Composable
fun MenuScreen(
    onSurveyClick: () -> Unit,
    onStatisticsClick: () -> Unit,
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
            Text(
                text = "Аналитика",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

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
        }
    }
}