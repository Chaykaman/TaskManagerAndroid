package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.data.local.entity.FabAlignment
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

interface AppSettingsRepository {
    val appTheme: Flow<AppTheme>
    val fabAlignment: Flow<FabAlignment>
    val streakMinTasks: Flow<Int>
    val streakRestDays: Flow<Set<DayOfWeek>>

    suspend fun saveAppTheme(theme: AppTheme)
    suspend fun saveFabAlignment(alignment: FabAlignment)
    suspend fun saveStreakMinTasks(count: Int)
    suspend fun saveStreakRestDays(days: Set<DayOfWeek>)
}