package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.example.taskmanager.feature.taskdetail.components.IconField

@Composable
fun AnimatedDirectionIcon(
    targetValue: Boolean,
    visible: Boolean
) {
    val rotation by animateFloatAsState(
        targetValue = if (targetValue) 0f else 180f,
        animationSpec = tween(durationMillis = 250),
        label = ""
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        IconField(
            icon = Icons.Default.ArrowUpward,
            modifier = Modifier.rotate(rotation)
        )
    }
}