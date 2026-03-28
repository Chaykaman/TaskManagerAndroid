package com.example.taskmanager.feature.habits.myhabitlist

import com.example.taskmanager.data.local.entity.habit.Habit

data class MyHabitListUiState(
    val activeHabits: List<Habit> = emptyList(),
    val archivedHabits: List<Habit> = emptyList(),

    val activeTab: MyHabitListTab = MyHabitListTab.ACTIVE,

    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val currentHabitList: List<Habit>
        get() = when (activeTab) {
            MyHabitListTab.ACTIVE -> activeHabits
            MyHabitListTab.ARCHIVED -> archivedHabits
        }
}
