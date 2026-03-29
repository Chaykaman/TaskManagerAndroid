package com.example.taskmanager.data.local.entity.achievement

enum class AchievementTrigger {
    TASK_CREATED,           // создана задача
    TASK_COMPLETED,         // выполнена задача
    HABIT_CREATED,          // создана привычка
    ALL_HABITS_COMPLETED,   // все привычки выполнены за день
    STREAK_REACHED          // достигнут стрик N дней
}