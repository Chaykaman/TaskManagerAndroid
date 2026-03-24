package com.example.taskmanager.feature.common

import androidx.compose.material3.FabPosition
import com.example.taskmanager.data.local.entity.FabAlignment

fun FabAlignment.toFabPosition(): FabPosition = when (this) {
    FabAlignment.START -> FabPosition.Start
    FabAlignment.CENTER -> FabPosition.Center
    FabAlignment.END -> FabPosition.End
}