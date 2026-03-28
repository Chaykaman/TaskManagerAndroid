package com.example.taskmanager.feature.habits.habitform.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.common.SectionHeader
import com.example.taskmanager.feature.common.TitleTextField

@Composable
fun HabitTitleField(
    title: String,
    focusRequester: FocusRequester,
    onTitleChange: (String) -> Unit,
    onDone: () -> Unit
) {
    SectionHeader(title = "Название")

    Box(modifier = Modifier.padding(8.dp)) {
        TitleTextField(
            value = title,
            placeholder = "Название привычки",
            focusRequester = focusRequester,
            onValueChange = onTitleChange,
            onDone = onDone
        )
    }
}