package com.example.taskmanager.feature.habits.myhabitlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.habits.habitform.HabitIcons
import com.example.taskmanager.feature.taskdetail.components.IconField

@Composable
fun MyHabitCard(
    title: String,
    iconName: String,
    color: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
            IconField(
                icon = HabitIcons.getIcon(iconName),
                tint = Color.White
            )

            HabitCardTextFields(
                title = title,
                isArchived = false
            )
        }
    }
}