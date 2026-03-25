package com.example.taskmanager.feature.common

import androidx.compose.runtime.compositionLocalOf
import com.example.taskmanager.data.local.entity.FabAlignment

val LocalFabAlignment = compositionLocalOf { FabAlignment.END }