package com.example.taskmanager.feature.habits.myhabitlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.habits.habitform.HabitIcons
import com.example.taskmanager.feature.taskdetail.components.IconField
import com.example.taskmanager.feature.tasklist.components.AnimatedDoneButton

@Composable
fun HabitCard(
    title: String,
    iconName: String,
    color: Long,
    isCompleted: Boolean,
    onDone: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(color),
            contentColor = Color.White,
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDone) {
                AnimatedDoneButton(
                    isCompleted = isCompleted,
                    color = Color.White
                )
            }

            HabitCardTextFields(
                title = title,
                isArchived = isCompleted
            )

            Spacer(modifier = Modifier.weight(1f))

            IconField(
                icon = HabitIcons.getIcon(iconName),
                tint = Color.White
            )
        }
    }
}