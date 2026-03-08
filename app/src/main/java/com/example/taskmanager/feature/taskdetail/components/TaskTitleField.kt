package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.taskmanager.feature.tasklist.components.animatedStrikeThrough

@Composable
fun TaskTitleField(
    value: String,
    onValueChange: (String) -> Unit,
    isCompleted: Boolean,
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.W500,
        ),
        onTextLayout = { textLayoutResult = it },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
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
            .animatedStrikeThrough(
                isCompleted = isCompleted,
                textLayoutResult = textLayoutResult,
                color = MaterialTheme.colorScheme.onSurface
            ),
    )
}