package com.example.taskmanager.feature.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.achievements.components.AchievementCard
import com.example.taskmanager.feature.achievements.components.AchievementProgressHeader

@Composable
fun AchievementContent(
    unlockedCount: Int,
    totalCount: Int,
    unlockedAchievements: List<AchievementUiItem>,
    lockedAchievements: List<AchievementUiItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Счётчик прогресса вверху
        AchievementProgressHeader(
            unlockedCount = unlockedCount,
            totalCount = totalCount
        )

        // Разблокированные достижения
        if (unlockedAchievements.isNotEmpty()) {
            Text(
                text = "Получено",
                style = MaterialTheme.typography.titleMedium
            )
            unlockedAchievements.forEach { item ->
                AchievementCard(item = item)
            }
        }

        // Заблокированные достижения
        if (lockedAchievements.isNotEmpty()) {
            Text(
                text = "В процессе",
                style = MaterialTheme.typography.titleMedium
            )
            lockedAchievements.forEach { item ->
                AchievementCard(item = item)
            }
        }
    }
}