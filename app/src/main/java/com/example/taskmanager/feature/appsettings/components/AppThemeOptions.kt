package com.example.taskmanager.feature.appsettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun AppThemeOptions(
    activeAppTheme: AppTheme?,
    onThemeChange: (AppTheme) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Светлая",
            icon = Icons.Rounded.LightMode,
            isActive = activeAppTheme == AppTheme.LIGHT,
            onThemeClick = { onThemeChange(AppTheme.LIGHT) }
        )
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Тёмная",
            icon = Icons.Rounded.DarkMode,
            isActive = activeAppTheme == AppTheme.DARK,
            onThemeClick = { onThemeChange(AppTheme.DARK) }
        )
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Авто",
            icon = Icons.Rounded.Smartphone,
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