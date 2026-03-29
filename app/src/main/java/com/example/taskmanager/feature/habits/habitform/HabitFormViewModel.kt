package com.example.taskmanager.feature.habits.habitform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.data.local.entity.habit.HabitFrequency
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.AchievementManager
import com.example.taskmanager.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HabitFormViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val achievementManager: AchievementManager,
    private val applicationScope: CoroutineScope
) : ViewModel() {

    init {
        TaskLogger.i("[HabitFormViewModel] Инициализирован")
    }

    private val _uiState = MutableStateFlow(HabitFormUiState())
    val uiState: StateFlow<HabitFormUiState> = _uiState.asStateFlow()

    fun initWith(habitId: Int?) {
        if (habitId == null) {
            _uiState.update { it.copy(mode = HabitFormMode.Create) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val habit = habitRepository.getHabitById(habitId)
            if (habit == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Привычка не найдена"
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    mode = HabitFormMode.Edit(habitId),
                    draftTitle = habit.title,
                    draftDescription = habit.description,
                    draftIconName = habit.iconName,
                    draftColor = habit.color,
                    draftFrequency = habit.frequency,
                    draftDaysOfWeek = habit.daysOfWeek.toSet(),
                    draftNotificationEnabled = habit.notificationEnabled,
                    isLoading = false
                )
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update {
            it.copy(
                draftTitle = title,
                titleError = null
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(draftDescription = description) }
    }

    fun onIconSelected(iconName: String) {
        _uiState.update { it.copy(draftIconName = iconName) }
    }

    fun onColorSelected(color: Long) {
        _uiState.update { it.copy(draftColor = color) }
    }

    fun onFrequencyChanged(frequency: HabitFrequency) {
        _uiState.update { currentState ->
            currentState.copy(
                draftFrequency = frequency,
                draftDaysOfWeek = if (frequency == HabitFrequency.DAILY)
                    emptySet() else currentState.draftDaysOfWeek,
                daysOfWeekError = null
            )
        }
    }

    fun onDayOfWeekToggled(day: DayOfWeek) {
        _uiState.update { currentState ->
            val newDays = if (day in currentState.draftDaysOfWeek) {
                currentState.draftDaysOfWeek - day
            } else {
                currentState.draftDaysOfWeek + day
            }
            currentState.copy(
                draftDaysOfWeek = newDays,
                daysOfWeekError = null
            )
        }
    }

    fun save() {
        if (!validate()) return

        // Добавляем привычку и обновляем прогресс выполнения
        // applicationScope живёт дольше, чем viewModelScope, который уничтожается после закрытия
        // страницы (из-за этого achievementManager падает с ошибкой). Поэтому всё записываем
        // в applicationScope, чтобы привычка добавилась, прогресс засчитался и чтобы всё
        // это происходило последовательно.
        applicationScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val currentState = _uiState.value

            when (val mode = currentState.mode) {
                is HabitFormMode.Create -> {
                    val newHabit = Habit(
                        title = currentState.draftTitle.trim(),
                        description = currentState.draftDescription.trim(),
                        iconName = currentState.draftIconName,
                        color = currentState.draftColor,
                        frequency = currentState.draftFrequency,
                        daysOfWeek = currentState.draftDaysOfWeek.sortedBy { it.value }
                    )
                    habitRepository.addHabit(newHabit)

                    // Обновляем прогресс для достижений
                    achievementManager.onHabitCreated()
                }
                is HabitFormMode.Edit -> {
                    val original = habitRepository.getHabitById(mode.habitId)
                        ?: return@launch

                    val updatedHabit = original.copy(
                        title = currentState.draftTitle.trim(),
                        description = currentState.draftDescription.trim(),
                        iconName = currentState.draftIconName,
                        color = currentState.draftColor,
                        frequency = currentState.draftFrequency,
                        daysOfWeek = currentState.draftDaysOfWeek
                            .sortedBy { it.value },
                        notificationEnabled = currentState.draftNotificationEnabled,
                        updatedAt = LocalDateTime.now()
                    )
                    habitRepository.updateHabit(updatedHabit)
                }
            }

            // Триггер для навигации назад
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }

    // Приватный метод валидации — заполняет поля ошибок в UiState
    private fun validate(): Boolean {
        val currentState = _uiState.value
        var isValid = true

        if (currentState.draftTitle.isBlank()) {
            _uiState.update { it.copy(titleError = "Название не может быть пустым") }
            isValid = false
        }

        if (currentState.draftFrequency == HabitFrequency.SPECIFIC_DAYS &&
            currentState.draftDaysOfWeek.isEmpty()
        ) {
            _uiState.update {
                it.copy(daysOfWeekError = "Выберите хотя бы один день")
            }
            isValid = false
        }

        return isValid
    }
}
