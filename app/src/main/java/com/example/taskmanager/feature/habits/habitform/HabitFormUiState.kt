package com.example.taskmanager.feature.habits.habitform

import com.example.taskmanager.data.local.entity.habit.HabitFrequency
import java.time.DayOfWeek

data class HabitFormUiState(
    val mode: HabitFormMode = HabitFormMode.Create,

    val draftTitle: String = "",
    val draftDescription: String = "",
    val draftIconName: String = HabitIcons.available.first().name,
    val draftColor: Long = HabitColors.available.first().value,
    val draftFrequency: HabitFrequency = HabitFrequency.DAILY,
    val draftDaysOfWeek: Set<DayOfWeek> = emptySet(),
    val draftNotificationEnabled: Boolean = false,

    val titleError: String? = null,
    val daysOfWeekError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false, // сигнал для навигации назад после сохранения
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = draftTitle.isNotBlank() &&
                (draftFrequency == HabitFrequency.DAILY || draftDaysOfWeek.isNotEmpty())
}