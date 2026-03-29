package com.example.taskmanager.data.local.entity.achievement

data class AchievementDefinition(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val level: AchievementLevel,
    val trigger: AchievementTrigger,
    val progressType: AchievementProgressType
)
