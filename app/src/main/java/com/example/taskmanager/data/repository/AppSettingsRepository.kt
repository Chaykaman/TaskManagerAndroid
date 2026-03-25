package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.data.local.entity.FabAlignment
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val appTheme: Flow<AppTheme>
    val fabAlignment: Flow<FabAlignment>

    suspend fun saveAppTheme(theme: AppTheme)
    suspend fun saveFabAlignment(alignment: FabAlignment)
}