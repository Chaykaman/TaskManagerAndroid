package com.example.taskmanager.feature.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.ScreenScaffold
import com.example.taskmanager.feature.menu.components.MenuHeader
import com.example.taskmanager.feature.taskdetail.components.IconField
import com.example.taskmanager.feature.taskdetail.components.RowField

@Composable
fun MenuScreen() {
    ScreenScaffold(
        topBar = {
            MenuHeader(
                onNotificationsClick = {},
                onSettingsClick = {},
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                text = "Действия",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            RowField(modifier = Modifier.clickable(onClick = {})) {
                IconField(
                    icon = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Поиск",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            RowField(modifier = Modifier.clickable(onClick = {})) {
                IconField(
                    icon = Icons.Default.History,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "История",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}