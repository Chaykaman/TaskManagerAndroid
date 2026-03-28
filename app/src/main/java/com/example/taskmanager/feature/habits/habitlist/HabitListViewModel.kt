package com.example.taskmanager.feature.habits.habitlist

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel  @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    init {
        TaskLogger.i("[HabitListViewModel] Инициализирован")
    }

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HabitListUiState> = _selectedDate
        .flatMapLatest { date ->
            habitRepository.getHabitsForToday()
                .combine(
                    habitRepository.getLogsForDate(date)
                ) { habits, logs ->
                    val completedIds = logs
                        .filter { it.isCompleted }
                        .map { it.habitId }
                        .toSet()

                    // Собираем составные элементы списка
                    val habitItems = habits.map { habit ->
                        HabitListItem(
                            habit = habit,
                            isCompletedToday = habit.id in completedIds
                        )
                    }

                    HabitListUiState(
                        habits = habitItems,
                        selectedDate = date,
                        completedCount = completedIds.size,
                        totalCount = habits.size,
                        isLoading = false
                    )
                }
        }
        .catch { exception ->
            emit(HabitListUiState(
                isLoading = false,
                errorMessage = "Не удалось загрузить привычки: ${exception.message}"
            ))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HabitListUiState(isLoading = true)
        )

    fun toggleHabitCompletion(habitId: Int) {
        viewModelScope.launch {
            habitRepository.toggleHabitCompletion(
                habitId = habitId,
                date = _selectedDate.value
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        if (!date.isAfter(LocalDate.now())) {
            _selectedDate.update { date }
        }
    }

    fun onPreviousDay() {
        _selectedDate.update { it.minusDays(1) }
    }

    fun onNextDay() {
        _selectedDate.update { current ->
            val next = current.plusDays(1)
            if (next.isAfter(LocalDate.now())) current else next
        }
    }
}