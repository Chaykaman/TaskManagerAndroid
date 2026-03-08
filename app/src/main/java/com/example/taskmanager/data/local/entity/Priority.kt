package com.example.taskmanager.data.local.entity

import androidx.compose.ui.graphics.Color

enum class Priority(
    val id: Int,
    val shortLabel: String,
    val label: String,
    val color: Color
) {
    PRIORITY_1(
        id = 1,
        shortLabel = "№1",
        label = "Срочный",
        color = Color(0xFFE53935)
    ),
    PRIORITY_2(
        id = 2,
        shortLabel = "№2",
        label = "Высокий",
        color = Color(0xFFFF8F00)
    ),
    PRIORITY_3(
        id = 3,
        shortLabel = "№3",
        label = "Средний",
        color = Color(0xFF1E88E5)
    ),
    PRIORITY_4(
        id = 4,
        shortLabel = "№4",
        label = "Обычный",
        color = Color(0xFF90A4AE)
    )
}