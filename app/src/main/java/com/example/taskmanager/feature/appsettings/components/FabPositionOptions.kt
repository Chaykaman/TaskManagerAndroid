package com.example.taskmanager.feature.appsettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.AlignHorizontalLeft
import androidx.compose.material.icons.automirrored.rounded.AlignHorizontalRight
import androidx.compose.material.icons.rounded.AlignHorizontalCenter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.FabAlignment

@Composable
fun FabPositionOptions(
    activeFabPosition: FabAlignment,
    onFabPositionChange: (FabAlignment) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Слева",
            icon = Icons.AutoMirrored.Rounded.AlignHorizontalLeft,
            isActive = activeFabPosition == FabAlignment.START,
            onThemeClick = { onFabPositionChange(FabAlignment.START) }
        )
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Центр",
            icon = Icons.Rounded.AlignHorizontalCenter,
            isActive = activeFabPosition == FabAlignment.CENTER,
            onThemeClick = { onFabPositionChange(FabAlignment.CENTER) }
        )
        SettingsOptionCard(
            modifier = Modifier.weight(1f),
            text = "Справа",
            icon = Icons.AutoMirrored.Rounded.AlignHorizontalRight,
            isActive = activeFabPosition == FabAlignment.END,
            onThemeClick = { onFabPositionChange(FabAlignment.END) }
        )
    }
}