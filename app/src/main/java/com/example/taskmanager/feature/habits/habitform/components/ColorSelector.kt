package com.example.taskmanager.feature.habits.habitform.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.feature.habits.habitform.HabitColors
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun ColorSelector(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(HabitColors.available) { habitColor ->
            val isSelected = habitColor.value == selectedColor
            val color = Color(habitColor.value)

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(habitColor.value) },
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn(
                        animationSpec = tween(200),
                        initialScale = 0.3f
                    ) + fadeIn(animationSpec = tween(200)),
                    exit = scaleOut(
                        animationSpec = tween(150),
                        targetScale = 0.3f
                    ) + fadeOut(animationSpec = tween(150))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorSelectorPreview() {
    TaskManagerTheme {
        ColorSelector(
            selectedColor = HabitColors.available[0].value,
            onColorSelected = {}
        )
    }
}