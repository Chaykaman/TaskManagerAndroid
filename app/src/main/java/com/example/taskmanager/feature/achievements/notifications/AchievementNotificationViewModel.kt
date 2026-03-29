package com.example.taskmanager.feature.achievements.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.achievement.AchievementDefinition
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.AchievementManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AchievementNotificationViewModel @Inject constructor(
    private val achievementManager: AchievementManager
) : ViewModel() {

    // Очередь уведомлений - если достижений получено несколько подряд
    private val _pendingAchievements = MutableStateFlow<List<AchievementDefinition>>(emptyList())
    val pendingAchievements: StateFlow<List<AchievementDefinition>> = _pendingAchievements.asStateFlow()

    init {
        observeNewAchievements()
        TaskLogger.i("[AchievementNotificationViewModel] Инициализирован")
    }

    private fun observeNewAchievements() {
        viewModelScope.launch {
            achievementManager.newlyUnlocked.collect { achievement ->
                _pendingAchievements.update { current -> current + achievement }
            }
        }
    }

    // Вызывается когда уведомление показано — убираем из очереди
    fun onAchievementShown() {
        _pendingAchievements.update { current -> current.drop(1) }
    }
}