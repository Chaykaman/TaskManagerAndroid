package com.example.taskmanager.feature.appsettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.AppTheme

@Composable
fun AppThemeSection(
    activeAppTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Тема приложения",
            style = MaterialTheme.typography.titleMedium,
        )

        AppThemeOptions(
            activeAppTheme = activeAppTheme,
            onThemeChange = onThemeChange
        )

        Text(
            text = when (activeAppTheme) {
                AppTheme.LIGHT -> "Приложение всегда использует светлую тему"
                AppTheme.DARK -> "Приложение всегда использует тёмную тему"
                AppTheme.SYSTEM -> "Тема зависит от настроек устройства"
            },
            style = MaterialTheme.typography.bodySmall,
        )
    }
}