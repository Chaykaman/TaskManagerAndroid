package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ChipField(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    icon: ImageVector,
    isActive: Boolean
) {
    val chipActiveColor = MaterialTheme.colorScheme.primary
    val chipInactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    AssistChip(
        onClick = onClick,
        label = label,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AssistChipDefaults.IconSize)
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            labelColor = if (isActive) chipActiveColor else chipInactiveColor,
            leadingIconContentColor = if (isActive) chipActiveColor else chipInactiveColor
        )
    )
}