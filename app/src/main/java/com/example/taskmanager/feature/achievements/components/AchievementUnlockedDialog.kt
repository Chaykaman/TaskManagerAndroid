package com.example.taskmanager.feature.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.achievement.AchievementDefinition
import com.example.taskmanager.feature.achievements.AchievementIcons

@Composable
fun AchievementUnlockedDialog(
    achievement: AchievementDefinition,
    onDismiss: () -> Unit
) {
    val levelColor = Color(achievement.level.color)

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(levelColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = AchievementIcons.getIcon(achievement.iconName),
                    contentDescription = null,
                    tint = levelColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = { Text("Достижение получено! 🎉") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LevelBadge(level = achievement.level)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Отлично!") }
        }
    )
}