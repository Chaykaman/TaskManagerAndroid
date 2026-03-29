package com.example.taskmanager.data.local.entity.achievement

sealed class AchievementProgressType {
    // Прогресс от 0 до target: "создано 7 из 10 задач"
    data class Counter(val target: Int) : AchievementProgressType()
    // Прогресс 0 или 1: "выполнено / не выполнено"
    object Boolean : AchievementProgressType()
}