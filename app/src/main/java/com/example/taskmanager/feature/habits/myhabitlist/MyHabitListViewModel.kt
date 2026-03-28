package com.example.taskmanager.feature.habits.myhabitlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MyHabitListViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    init {
        TaskLogger.i("[MyHabitListViewModel] Инициализирован")
    }

    private val _activeTab = MutableStateFlow(MyHabitListTab.ACTIVE)

    val uiState: StateFlow<MyHabitListUiState> = combine(
        habitRepository.getActiveHabits(),
        habitRepository.getArchivedHabits(),
        _activeTab
    ) { activeHabits, archivedHabits, activeTab ->
        MyHabitListUiState(
            activeHabits = activeHabits,
            archivedHabits = archivedHabits,
            activeTab = activeTab,
            isLoading = false
        )
    }
        .catch { exception ->
            emit(MyHabitListUiState(
                isLoading = false,
                errorMessage = "Не удалось загрузить привычки: ${exception.message}"
            ))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyHabitListUiState(isLoading = true)
        )

    fun onTabSelected(tab: MyHabitListTab) {
        _activeTab.update { tab }
    }

    fun toggleArchive(habit: Habit) {
        viewModelScope.launch {
            habitRepository.updateHabit(
                habit.copy(
                    isArchived = !habit.isArchived,
                    updatedAt = LocalDateTime.now()
                )
            )
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habit)
        }
    }
}