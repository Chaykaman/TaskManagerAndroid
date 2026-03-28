package com.example.taskmanager.feature.habits.habitform.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.habits.habitform.HabitIcons
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun IconSelector(
    selectedIconName: String,
    onIconSelected: (String) -> Unit
) {
    val colorDefault = MaterialTheme.colorScheme.surfaceVariant
    val colorContainerSelected = MaterialTheme.colorScheme.primaryContainer
    val colorContentSelected = MaterialTheme.colorScheme.primary
    val shape = MaterialTheme.shapes.medium

    LazyRow(
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(HabitIcons.available) { habitIcon ->
            val isSelected = habitIcon.name == selectedIconName
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape)
                    .background(
                        if (isSelected) colorContainerSelected else colorDefault
                    )
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) colorContentSelected else colorDefault,
                        shape = shape
                    )
                    .clickable { onIconSelected(habitIcon.name) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = habitIcon.icon,
                    contentDescription = habitIcon.name,
                    tint = if (isSelected) colorContentSelected
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IconSelectorPreview() {
    TaskManagerTheme {
        IconSelector(
            selectedIconName = HabitIcons.available[0].name,
            onIconSelected = {}
        )
    }
}