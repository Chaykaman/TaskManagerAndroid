package com.example.taskmanager.feature.appsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.data.local.entity.FabAlignment
import com.example.taskmanager.data.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val settingsRepository: AppSettingsRepository
) : ViewModel() {

    val uiState: StateFlow<AppSettingsUiState> = combine(
        settingsRepository.appTheme,
        settingsRepository.fabAlignment
    ) { theme, fabAlignment ->
        AppSettingsUiState(
            appTheme = theme,
            fabAlignment = fabAlignment
        )
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettingsUiState()
    )

    /**
     * Обновление темы приложения (светлая, тёмная, системная)
     * @param theme Тема приложения
     */
    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.saveAppTheme(theme)
        }
    }

    /**
     * Обновление расположения кнопки добавления задач
     * @param alignment Расположение кнопки добавления задач
     */
    fun setFabAlignment(alignment: FabAlignment) {
        viewModelScope.launch {
            settingsRepository.saveFabAlignment(alignment)
        }
    }
}