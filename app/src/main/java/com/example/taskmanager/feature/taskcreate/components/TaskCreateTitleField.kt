package com.example.taskmanager.feature.taskcreate.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TaskCreateTitleField(
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.W500,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { if (value.isNotBlank()) onDone() }
        ),
        decorationBox = { inner ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = "Название задачи",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.W500,
                        )
                    )
                }
                inner()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
    )
}