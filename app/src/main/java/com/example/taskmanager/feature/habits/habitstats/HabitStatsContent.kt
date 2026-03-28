package com.example.taskmanager.feature.habits.habitstats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.example.taskmanager.data.local.entity.habit.HabitStatEntry
import com.example.taskmanager.feature.habits.habitstats.components.HabitBarChart
import com.example.taskmanager.feature.habits.habitstats.components.HabitLineChart
import com.example.taskmanager.feature.habits.habitstats.components.HabitPieChart
import com.example.taskmanager.feature.habits.habitstats.components.HabitStatRow
import com.example.taskmanager.feature.habits.habitstats.components.OverallCompletionCard
import com.example.taskmanager.feature.habits.habitstats.components.PeriodSelector
import com.example.taskmanager.feature.habits.habitstats.components.StatsSection

@Composable
fun HabitStatsContent(
    selectedPeriod: StatsPeriod,
    onPeriodSelected: (StatsPeriod) -> Unit,
    overallCompletionRate: Float,
    totalCompleted: Int,
    totalPossible: Int,
    dailyCompletions: List<DayCompletionCount>,
    habitStats: List<HabitStatEntry>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = onPeriodSelected
        )

        OverallCompletionCard(
            completionRate = overallCompletionRate,
            totalCompleted = totalCompleted,
            totalPossible = totalPossible
        )

        StatsSection(title = "Активность по дням") {
            HabitBarChart(dailyCompletions = dailyCompletions)
        }

        // Динамика прогресса
        StatsSection(title = "Динамика прогресса") {
            HabitLineChart(
                dailyCompletions = dailyCompletions,
                totalHabitsCount = habitStats.size.coerceAtLeast(1)
            )
        }

        // Топ привычек
        StatsSection(title = "Распределение по привычкам") {
            HabitPieChart(habitStats = habitStats)
        }

        // Детальный список всех привычек со статистикой
        StatsSection(title = "По каждой привычке") {
            habitStats.forEach { stat ->
                HabitStatRow(stat = stat)
            }
        }
    }
}