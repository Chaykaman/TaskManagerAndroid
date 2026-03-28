package com.example.taskmanager.feature.habits.myhabitlist.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.taskmanager.feature.tasklist.components.animatedStrikeThrough

@Composable
fun HabitCardTextFields(
    title: String,
    isArchived: Boolean
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult = it },
        modifier = Modifier.animatedStrikeThrough(
            isCompleted = isArchived,
            textLayoutResult = textLayoutResult,
            color = Color.White
        )
    )
}