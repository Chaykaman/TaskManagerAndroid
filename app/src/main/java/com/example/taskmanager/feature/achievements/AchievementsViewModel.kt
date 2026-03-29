package com.example.taskmanager.feature.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.dao.AchievementDao
import com.example.taskmanager.data.local.entity.achievement.AchievementDefinitions
import com.example.taskmanager.data.local.entity.achievement.AchievementProgress
import com.example.taskmanager.data.repository.AchievementManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementDao: AchievementDao,
    private val achievementManager: AchievementManager,
    private val applicationScope: CoroutineScope
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            achievementManager.initializeIfNeeded()
        }
        observeAchievements()
    }

    private fun observeAchievements() {
        viewModelScope.launch {
            achievementDao.getAllProgress().collect { progressList ->
                // Объединяем прогресс из БД с определениями из кода
                val progressMap = progressList.associateBy { it.achievementId }

                val items = AchievementDefinitions.all.map { definition ->
                    AchievementUiItem(
                        definition = definition,
                        // Если записи нет — создаём пустой прогресс
                        progress = progressMap[definition.id]
                            ?: AchievementProgress(achievementId = definition.id)
                    )
                }

                _uiState.update {
                    it.copy(
                        unlockedAchievements = items.filter { item -> item.isUnlocked }
                            .sortedByDescending { item -> item.progress.unlockedAt },
                        lockedAchievements = items.filter { item -> !item.isUnlocked }
                            .sortedBy { item -> item.progressFraction * -1 },
                        isLoading = false
                    )
                }
            }
        }
    }

//    private fun observeNewlyUnlocked() {
//        viewModelScope.launch {
//            achievementManager.newlyUnlocked.collect { definition ->
//                _uiState.update { it.copy(newlyUnlockedAchievement = definition) }
//            }
//        }
//    }

    // Вызывается после показа попапа — сбрасываем уведомление
//    fun onAchievementNotificationShown() {
//        _uiState.update { it.copy(newlyUnlockedAchievement = null) }
//    }

    fun resetAllAchievements() {
        applicationScope.launch {
            achievementManager.resetAllAchievements()
        }
    }
}