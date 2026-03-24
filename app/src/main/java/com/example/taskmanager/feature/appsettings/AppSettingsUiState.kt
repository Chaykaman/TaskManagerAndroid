package com.example.taskmanager.feature.appsettings

import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.data.local.entity.FabAlignment

data class AppSettingsUiState(
    val appTheme: AppTheme? = null,
    val fabAlignment: FabAlignment = FabAlignment.END,
    val isLoading: Boolean = true
)
