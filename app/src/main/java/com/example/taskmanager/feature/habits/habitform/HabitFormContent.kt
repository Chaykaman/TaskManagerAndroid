package com.example.taskmanager.feature.habits.habitform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.example.taskmanager.data.local.entity.habit.HabitFrequency
import com.example.taskmanager.feature.habits.habitform.components.HabitColorField
import com.example.taskmanager.feature.habits.habitform.components.HabitFrequencyField
import com.example.taskmanager.feature.habits.habitform.components.HabitIconField
import com.example.taskmanager.feature.habits.habitform.components.HabitTitleField
import java.time.DayOfWeek

@Composable
fun HabitFormContent(
    innerPadding: PaddingValues,
    focusRequester: FocusRequester,
    habitTitle: String,
    habitIconName: String,
    habitColor: Long,
    habitFrequency: HabitFrequency,
    habitDaysOfWeek: Set<DayOfWeek>,
    onTitleChange: (String) -> Unit,
    onIconSelected: (String) -> Unit,
    onColorSelected: (Long) -> Unit,
    onFrequencyChanged: (HabitFrequency) -> Unit,
    onDayClick: (DayOfWeek) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
    ) {
        HabitTitleField(
            title = habitTitle,
            focusRequester = focusRequester,
            onTitleChange = onTitleChange,
            onDone = onDone
        )

        HabitIconField(
            habitIconName = habitIconName,
            onIconSelected = onIconSelected
        )

        HabitColorField(
            habitColor = habitColor,
            onColorSelected = onColorSelected
        )

        HabitFrequencyField(
            selectedFrequency = habitFrequency,
            selectedDays = habitDaysOfWeek,
            onFrequencyChanged = onFrequencyChanged,
            onDayClick = onDayClick
        )
    }
}