package com.example.taskmanager.data.local.entity.habit

data class HabitStatEntry(
    val habitId: Long,
    val habitTitle: String,
    val iconName: String,
    val color: Long,
    val completedDays: Int,
    val totalDays: Int
) {
    // Вычисляемое свойство — процент выполнения от 0.0 до 1.0
    val completionRate: Float
        get() = if (totalDays == 0) 0f else completedDays.toFloat() / totalDays.toFloat()
}
