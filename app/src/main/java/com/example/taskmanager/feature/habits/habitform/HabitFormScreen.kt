package com.example.taskmanager.feature.habits.habitform

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar
import com.example.taskmanager.feature.common.topappbar.TopAppAction

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HabitFormScreen(
    viewModel: HabitFormViewModel = hiltViewModel(),
    habitId: Int? = null,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Подгрузка привычки
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(habitId) {
        if (habitId == null) {
            focusRequester.requestFocus()
            return@LaunchedEffect
        }

        viewModel.initWith(habitId)
    }

    // Закрытие страницы
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBack()
    }

    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Привычка",
                onBack = onBack,
                actions = listOf(
                    TopAppAction(
                        icon = Icons.Rounded.Done,
                        contentDescription = "Сохранить",
                        enabled = uiState.isFormValid,
                        onClick = viewModel::save,
                    )
                )
            )
        }
    ) { innerPadding ->
        HabitFormContent(
            innerPadding = innerPadding,
            focusRequester = focusRequester,
            habitTitle = uiState.draftTitle,
            habitIconName = uiState.draftIconName,
            habitColor = uiState.draftColor,
            habitFrequency = uiState.draftFrequency,
            habitDaysOfWeek = uiState.draftDaysOfWeek,
            onTitleChange = viewModel::onTitleChanged,
            onIconSelected = viewModel::onIconSelected,
            onColorSelected = viewModel::onColorSelected,
            onFrequencyChanged = viewModel::onFrequencyChanged,
            onDayClick = viewModel::onDayOfWeekToggled,
            onDone = viewModel::save
        )
    }
}