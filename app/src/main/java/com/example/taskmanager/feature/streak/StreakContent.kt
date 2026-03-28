package com.example.taskmanager.feature.streak

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.DayTaskCount
import com.example.taskmanager.feature.habits.habitstats.components.StatsSection
import com.example.taskmanager.feature.streak.components.CurrentStreakCard
import com.example.taskmanager.feature.streak.components.MaxStreakCard
import com.example.taskmanager.feature.streak.components.MinTasksSelector
import com.example.taskmanager.feature.streak.components.RestDaysSelector
import com.example.taskmanager.feature.streak.components.WeeklyTasksChart
import java.time.DayOfWeek

@Composable
fun StreakContent(
    modifier: Modifier = Modifier,
    currentStreak: Int,
    maxStreak: Int,
    maxStreakPeriod: String?,
    minTasksPerDay: Int,
    restDays: Set<DayOfWeek>,
    weeklyTaskCounts: List<DayTaskCount>,
    onSetMinTasksPerDay: (Int) -> Unit,
    onToggleRestDay: (DayOfWeek) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CurrentStreakCard(currentStreak = currentStreak)

        StatsSection(title = "Лучшая серия") {
            MaxStreakCard(
                maxStreak = maxStreak,
                period = maxStreakPeriod
            )
        }

        StatsSection(title = "Задачи за неделю") {
            WeeklyTasksChart(
                weeklyTaskCounts = weeklyTaskCounts,
                minTasksPerDay = minTasksPerDay
            )
        }

        StatsSection(title = "Минимум задач в день") {
            MinTasksSelector(
                current = minTasksPerDay,
                onChanged = onSetMinTasksPerDay
            )
        }

        StatsSection(title = "Выходные дни") {
            RestDaysSelector(
                selectedDays = restDays,
                onDayToggled = onToggleRestDay
            )
        }
    }
}