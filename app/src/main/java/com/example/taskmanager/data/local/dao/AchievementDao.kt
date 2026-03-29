package com.example.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.local.entity.achievement.AchievementProgress
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProgress(progress: AchievementProgress)

    @Update
    suspend fun updateProgress(progress: AchievementProgress)

    // IGNORE а не REPLACE чтобы не перезаписать разблокированные достижения
    @Query("SELECT * FROM achievement_progress WHERE achievementId = :achievementId")
    suspend fun getProgress(achievementId: String): AchievementProgress?

    @Query("SELECT * FROM achievement_progress")
    fun getAllProgress(): Flow<List<AchievementProgress>>

    // Обновляем прогресс только если достижение ещё не разблокировано.
    @Query("""
        UPDATE achievement_progress 
        SET currentProgress = :progress,
            isUnlocked = :isUnlocked,
            unlockedAt = CASE WHEN :isUnlocked = 1 AND unlockedAt IS NULL 
                              THEN :unlockedAt ELSE unlockedAt END
        WHERE achievementId = :achievementId AND isUnlocked = 0
    """)
    suspend fun updateProgressIfNotUnlocked(
        achievementId: String,
        progress: Int,
        isUnlocked: Boolean,
        unlockedAt: LocalDate?
    )

    @Query("DELETE FROM achievement_progress")
    suspend fun clearAllProgress()
}