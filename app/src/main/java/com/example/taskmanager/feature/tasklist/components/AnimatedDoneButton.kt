package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedDoneButton(
    isCompleted: Boolean,
    color: Color,
) {
    AnimatedContent(
        targetState = isCompleted,
        transitionSpec = {
            (fadeIn(tween(150)) + scaleIn(
                initialScale = 0.6f,
                animationSpec = tween(150)
            )) togetherWith
                    (fadeOut(tween(100)) + scaleOut(
                        targetScale = 0.6f,
                        animationSpec = tween(100)
                    ))
        },
        label = "AnimatedDoneButton"
    ) { isCompleted ->
        Icon(
            imageVector = if (isCompleted)
                Icons.Filled.CheckCircle
            else
                Icons.Outlined.Circle,
            contentDescription = null,
            tint = color
        )
    }
}