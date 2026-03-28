package com.example.taskmanager.feature.streak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.repository.AppSettingsRepository
import com.example.taskmanager.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StreakViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val settingsRepository: AppSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StreakUiState())
    val uiState: StateFlow<StreakUiState> = _uiState.asStateFlow()

    init {
        observeStreakData()
    }

    private fun observeStreakData() {
        viewModelScope.launch {
            // Объединяем настройки — они могут меняться в реальном времени
            combine(
                settingsRepository.streakMinTasks,
                settingsRepository.streakRestDays
            ) { minTasks, restDays -> Pair(minTasks, restDays) }
                .collect { (minTasks, restDays) ->
                    loadStreakData(minTasks, restDays)
                }
        }
    }

    private suspend fun loadStreakData(minTasks: Int, restDays: Set<DayOfWeek>) {
        _uiState.update { it.copy(isLoading = true) }

        try {
            // Данные за всё время для вычисления стриков
            val allCompletedDates = taskRepository.getAllCompletedDates()

            // Данные за последние 365 дней — достаточно для любого реального стрика
            val yearAgo = LocalDate.now().minusDays(364)
            val today = LocalDate.now()
            val completedPerDay = taskRepository
                .getCompletedTasksPerDay(yearAgo, today)
                .associate { it.date to it.completedCount }

            // Вычисляем стрики через наш калькулятор
            val streakResult = StreakCalculator.calculate(
                completedPerDay = completedPerDay,
                minTasksPerDay = minTasks,
                restDays = restDays
            )

            // Данные для графика — только текущая неделя
            val weekStart = today.with(DayOfWeek.MONDAY)
            val weeklyData = taskRepository.getCompletedTasksPerDay(weekStart, today)

            _uiState.update {
                it.copy(
                    currentStreak = streakResult.currentStreak,
                    maxStreak = streakResult.maxStreak,
                    maxStreakStartDate = streakResult.maxStreakStartDate,
                    maxStreakEndDate = streakResult.maxStreakEndDate,
                    weeklyTaskCounts = weeklyData,
                    minTasksPerDay = minTasks,
                    restDays = restDays,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить данные: ${e.message}"
                )
            }
        }
    }

    fun setMinTasksPerDay(count: Int) {
        viewModelScope.launch {
            settingsRepository.saveStreakMinTasks(count)
        }
    }

    fun toggleRestDay(day: DayOfWeek) {
        viewModelScope.launch {
            val current = _uiState.value.restDays
            val newDays = if (day in current) current - day else current + day
            settingsRepository.saveStreakRestDays(newDays)
        }
    }
}