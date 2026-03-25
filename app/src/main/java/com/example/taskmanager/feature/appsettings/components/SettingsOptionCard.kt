package com.example.taskmanager.feature.appsettings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun SettingsOptionCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    isActive: Boolean,
    onThemeClick: () -> Unit
) {
    val contentColor = when (isActive) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.onSurface
    }

    val containerColor = when (isActive) {
        true -> MaterialTheme.colorScheme.primaryContainer
        false -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when (isActive) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.outline
    }

    OutlinedCard(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onThemeClick),
        colors = CardDefaults.outlinedCardColors(
            contentColor = contentColor,
            containerColor = containerColor,
        ),
        border = BorderStroke(width = 1.dp, color = borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text
            )
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsOptionCardPreview() {
    TaskManagerTheme {
        SettingsOptionCard(
            text = "Auto",
            icon = Icons.Default.Palette,
            isActive = true,
            onThemeClick = {}
        )
    }
}