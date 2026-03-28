package com.example.taskmanager.feature.appsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.appsettings.components.AppThemeSection
import com.example.taskmanager.feature.appsettings.components.FabPositionSection

@Composable
fun AppSettingsScreen(
    viewModel: AppSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W500
        )

        AppThemeSection(
            activeAppTheme = uiState.appTheme,
            onThemeChange = viewModel::setAppTheme
        )

        FabPositionSection(
            activeFabPosition = uiState.fabAlignment,
            onFabPositionChange = viewModel::setFabAlignment
        )
    }
}