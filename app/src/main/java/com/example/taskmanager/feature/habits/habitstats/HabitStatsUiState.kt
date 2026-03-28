package com.example.taskmanager.feature.habits.habitstats

import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.example.taskmanager.data.local.entity.habit.HabitStatEntry
import java.time.LocalDate

data class HabitStatsUiState(
    val selectedPeriod: StatsPeriod = StatsPeriod.WEEK,
    val overallCompletionRate: Float = 0f,

    val totalCompleted: Int = 0,
    val totalPossible: Int = 0,

    val dailyCompletions: List<DayCompletionCount> = emptyList(),
    val habitStats: List<HabitStatEntry> = emptyList(),

    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val topHabits: List<HabitStatEntry>
        get() = habitStats.take(3)

    val periodStartDate: LocalDate
        get() = when (selectedPeriod) {
            StatsPeriod.WEEK -> LocalDate.now().minusDays(6)
            StatsPeriod.MONTH -> LocalDate.now().minusDays(29)
        }
    val periodEndDate: LocalDate get() = LocalDate.now()
}
