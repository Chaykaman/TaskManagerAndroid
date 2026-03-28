package com.example.taskmanager.feature.habits.habitstats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitStatsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    init {
        TaskLogger.i("[HabitStatsViewModel] Инициализирован")
    }

    private val _selectedPeriod = MutableStateFlow(StatsPeriod.WEEK)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HabitStatsUiState> = _selectedPeriod
        .flatMapLatest { period ->
            flow {
                emit(HabitStatsUiState(isLoading = true, selectedPeriod = period))

                val endDate = LocalDate.now()
                val startDate = when (period) {
                    StatsPeriod.WEEK -> endDate.minusDays(6)
                    StatsPeriod.MONTH -> endDate.minusDays(29)
                }

                val dailyCompletions = habitRepository.getCompletionsPerDay(startDate, endDate)
                val habitStats = habitRepository.getHabitStatsForPeriod(startDate, endDate)

                val totalCompleted = habitStats.sumOf { it.completedDays }
                val totalPossible = habitStats.sumOf { it.totalDays }
                val overallRate = if (totalPossible == 0) 0f
                else totalCompleted.toFloat() / totalPossible.toFloat()

                emit(HabitStatsUiState(
                    selectedPeriod = period,
                    overallCompletionRate = overallRate,
                    totalCompleted = totalCompleted,
                    totalPossible = totalPossible,
                    dailyCompletions = dailyCompletions,
                    habitStats = habitStats.sortedByDescending { it.completionRate },
                    isLoading = false
                ))
            }
        }
        .catch { exception ->
            emit(HabitStatsUiState(
                isLoading = false,
                errorMessage = "Не удалось загрузить статистику: ${exception.message}"
            ))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HabitStatsUiState(isLoading = true)
        )

    fun onPeriodSelected(period: StatsPeriod) {
        _selectedPeriod.update { period }
    }
}