package com.example.taskmanager.feature.achievements.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.achievement.AchievementLevel

@Composable
fun LevelBadge(level: AchievementLevel) {
    val (text, color) = when (level) {
        AchievementLevel.BRONZE -> "Bronze" to Color(0xFFCD7F32)
        AchievementLevel.SILVER -> "Silver" to Color(0xFF808080)
        AchievementLevel.GOLD -> "Gold" to Color(0xFFCCAB00)
    }
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}