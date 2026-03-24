package com.example.taskmanager.feature.appsettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.FabAlignment

@Composable
fun FabPositionSection(
    activeFabPosition: FabAlignment,
    onFabPositionChange: (FabAlignment) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Позиция кнопки",
            style = MaterialTheme.typography.titleMedium,
        )

        FabPositionOptions(
            activeFabPosition = activeFabPosition,
            onFabPositionChange = onFabPositionChange
        )

        Text(
            text = when (activeFabPosition) {
                FabAlignment.START -> "Кнопка добавления задач размещена слева"
                FabAlignment.CENTER -> "Кнопка добавления задач размещена по центру"
                FabAlignment.END -> "Кнопка добавления задач размещена справа"
            },
            style = MaterialTheme.typography.bodySmall,
        )
    }
}