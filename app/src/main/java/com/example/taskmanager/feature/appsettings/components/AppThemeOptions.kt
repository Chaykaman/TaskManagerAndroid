package com.example.taskmanager.feature.appsettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun AppThemeOptions(
    activeAppTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Светлая",
            icon = Icons.Default.LightMode,
            isActive = activeAppTheme == AppTheme.LIGHT,
            onThemeClick = { onThemeChange(AppTheme.LIGHT) }
        )
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Тёмная",
            icon = Icons.Default.DarkMode,
            isActive = activeAppTheme == AppTheme.DARK,
            onThemeClick = { onThemeChange(AppTheme.DARK) }
        )
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Авто",
            icon = Icons.Default.Smartphone,
            isActive = activeAppTheme == AppTheme.SYSTEM,
            onThemeClick = { onThemeChange(AppTheme.SYSTEM) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppThemeOptionsPreview() {
    TaskManagerTheme {
        AppThemeOptions(
            activeAppTheme = AppTheme.SYSTEM,
            onThemeChange = {}
        )
    }
}