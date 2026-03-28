package com.example.taskmanager.feature.habits.habitlist

import java.time.LocalDate

open class HabitFormMode {
    object Create : HabitFormMode()
    data class Edit(val habitId: Int) : HabitFormMode()
}

data class HabitListUiState(
    val habits: List<HabitListItem> = emptyList(),

    val selectedDate: LocalDate = LocalDate.now(),

    val completedCount: Int = 0,
    val totalCount: Int = 0,

    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val completionProgress: Float
        get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()

    val isAllCompleted: Boolean
        get() = totalCount > 0 && completedCount == totalCount
}
