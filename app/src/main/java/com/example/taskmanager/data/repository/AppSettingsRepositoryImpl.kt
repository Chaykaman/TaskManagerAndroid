package com.example.taskmanager.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.taskmanager.data.di.AppSettingsDataStore
import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.data.local.entity.FabAlignment
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    @AppSettingsDataStore private val dataStore: DataStore<Preferences>
) : AppSettingsRepository {

    init {
        TaskLogger.i("[SettingsRepositoryImpl] Инициализирован")
    }

    private object Keys {
        val APP_THEME = stringPreferencesKey("app_theme")
        val FAB_ALIGNMENT = stringPreferencesKey("fab_alignment")
    }

    // Поле темы приложения
    override val appTheme: Flow<AppTheme> = dataStore.data.map { preferences ->
        preferences[Keys.APP_THEME]
            ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
            ?: AppTheme.SYSTEM
    }

    // Поле позиции кнопки
    override val fabAlignment: Flow<FabAlignment> = dataStore.data.map { preferences ->
        preferences[Keys.FAB_ALIGNMENT]
            ?.let { runCatching { FabAlignment.valueOf(it) }.getOrNull() }
            ?: FabAlignment.END
    }

    /**
     * Обновление темы приложения
     * @param theme Новое значение темы приложения
     */
    override suspend fun saveAppTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[Keys.APP_THEME] = theme.name
        }
    }

    /**
     * Обновление позиции расположения кнопки FAB
     * @param alignment Новое значение позиции расположения кнопки FAB
     */
    override suspend fun saveFabAlignment(alignment: FabAlignment) {
        dataStore.edit { preferences ->
            preferences[Keys.FAB_ALIGNMENT] = alignment.name
        }
    }
}