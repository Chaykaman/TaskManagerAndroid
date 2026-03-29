package com.example.taskmanager.feature.achievements

import com.example.taskmanager.data.local.entity.achievement.AchievementDefinition
import com.example.taskmanager.data.local.entity.achievement.AchievementProgress
import com.example.taskmanager.data.local.entity.achievement.AchievementProgressType

data class AchievementUiItem(
    val definition: AchievementDefinition,
    val progress: AchievementProgress
) {
    val isUnlocked: Boolean get() = progress.isUnlocked

    // Прогресс от 0.0 до 1.0 для ProgressBar
    val progressFraction: Float
        get() = when (val type = definition.progressType) {
            is AchievementProgressType.Counter ->
                progress.currentProgress.toFloat() / type.target.toFloat()
            is AchievementProgressType.Boolean ->
                if (progress.isUnlocked) 1f else 0f
        }

    // Текст прогресса: "7 / 10" или "Выполнено"
    val progressText: String
        get() = when (val type = definition.progressType) {
            is AchievementProgressType.Counter ->
                "${progress.currentProgress} / ${type.target}"
            is AchievementProgressType.Boolean ->
                if (progress.isUnlocked) "Выполнено" else "Не выполнено"
        }
}
