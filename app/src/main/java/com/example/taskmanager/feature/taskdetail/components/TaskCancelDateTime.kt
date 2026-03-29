package com.example.taskmanager.feature.taskdetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskCancelDateTime(
    text: String,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        AssistChip(
            onClick = onCancel,
            label = { Text(text = text) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = "Сбросить",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = MaterialTheme.colorScheme.error,
                leadingIconContentColor = MaterialTheme.colorScheme.error,

                ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.error
            )
        )
    }
}