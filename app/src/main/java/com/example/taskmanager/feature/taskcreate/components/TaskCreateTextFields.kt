package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp

@Composable
fun TaskCreateTextFields(
    title: String,
    description: String,
    focusRequester: FocusRequester,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TaskCreateTitleField(
            value = title,
            onValueChange = onTitleChange,
            focusRequester = focusRequester,
            onDone = onSubmit
        )

        TaskCreateDescriptionField(
            value = description,
            onValueChange = onDescriptionChange
        )
    }
}