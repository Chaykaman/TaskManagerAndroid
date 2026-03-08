package com.example.taskmanager.data.local.entity

import androidx.compose.ui.graphics.Color

enum class Status(
    val id: Int,
    val label: String,
    val color: Color
) {
    TODO(
        id = 1,
        label = "Сделать",
        color = Color(0xFFE53935)
    ),
    IN_PROGRESS(
        id = 2,
        label = "В работе",
        color = Color(0xFFFF8F00)
    ),
    DONE(
        id = 3,
        label = "Готово",
        color = Color(0xFF1E88E5)
    ),
}