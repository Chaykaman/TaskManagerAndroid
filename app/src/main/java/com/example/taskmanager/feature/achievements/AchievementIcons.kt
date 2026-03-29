package com.example.taskmanager.feature.achievements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Mood
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector

object AchievementIcons {

    data class AchievementIcon(val name: String, val icon: ImageVector)

    val available = listOf(
        AchievementIcon("add_task", Icons.Rounded.AddTask),
        AchievementIcon("task_alt", Icons.Rounded.TaskAlt),
        AchievementIcon("repeat", Icons.Rounded.Repeat),
        AchievementIcon("mood", Icons.Rounded.Mood),
        AchievementIcon("local_fire_department", Icons.Rounded.LocalFireDepartment),
    )

    fun getIcon(name: String): ImageVector =
        available.find { it.name == name }?.icon ?: Icons.Default.Star
}