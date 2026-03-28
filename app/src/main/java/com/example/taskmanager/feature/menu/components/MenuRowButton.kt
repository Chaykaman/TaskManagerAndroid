package com.example.taskmanager.feature.menu.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.taskmanager.feature.taskdetail.components.IconField
import com.example.taskmanager.feature.taskdetail.components.RowField

@Composable
fun MenuRowButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
) {
    RowField(modifier = Modifier.clickable(onClick = onClick)) {
        IconField(
            icon = icon,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}