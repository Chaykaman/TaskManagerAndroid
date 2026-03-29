package com.example.taskmanager.feature.achievements

data class AchievementsUiState(
    // Разблокированные достижения вверху — мотивирует видеть результат
    val unlockedAchievements: List<AchievementUiItem> = emptyList(),
    val lockedAchievements: List<AchievementUiItem> = emptyList(),

    val isLoading: Boolean = true
) {
    val totalCount: Int get() = unlockedAchievements.size + lockedAchievements.size
    val unlockedCount: Int get() = unlockedAchievements.size
}
