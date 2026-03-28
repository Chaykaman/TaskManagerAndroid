package com.example.taskmanager.feature.streak

import com.example.taskmanager.data.local.entity.DayTaskCount
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class StreakUiState(
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val maxStreakStartDate: LocalDate? = null,
    val maxStreakEndDate: LocalDate? = null,

    // Данные для графика — задачи по дням текущей недели
    val weeklyTaskCounts: List<DayTaskCount> = emptyList(),

    // Настройки
    val minTasksPerDay: Int = 1,
    val restDays: Set<DayOfWeek> = emptySet(),

    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    // Форматированный диапазон максимального стрика для UI
    val maxStreakPeriod: String?
        get() {
            val start = maxStreakStartDate ?: return null
            val end = maxStreakEndDate ?: return null
            val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))
            return "${start.format(formatter)} — ${end.format(formatter)}"
        }
}
