package com.example.taskmanager.data.local.entity.achievement

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "achievement_progress")
data class AchievementProgress(
    @PrimaryKey
    val achievementId: String, // ключ из AchievementDefinition

    // Текущее значение прогресса.
    // Для Counter - количество (0, 7, 10...)
    // Для Boolean - 0 или 1
    val currentProgress: Int = 0,

    // Разблокировано ли достижение
    val isUnlocked: Boolean = false,

    // Когда было разблокировано - для отображения даты получения
    val unlockedAt: LocalDate? = null
)
