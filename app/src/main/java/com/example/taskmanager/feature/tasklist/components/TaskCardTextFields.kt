package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@Composable
fun TaskCardTextFields(
    title: String,
    description: String,
    dueDate: LocalDate?,
    formattedDueDate: String,
    isCompleted: Boolean,
    isOverdue: Boolean,
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val scheme = MaterialTheme.colorScheme
    val tintColor = if (isOverdue) scheme.error else scheme.primary

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .animateContentSize()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult = it },
            modifier = Modifier.animatedStrikeThrough(
                isCompleted = isCompleted,
                textLayoutResult = textLayoutResult,
                color = scheme.onSurface
            )
        )

        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (dueDate != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = formattedDueDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = tintColor,
                )
            }
        }
    }
}