package com.example.taskmanager.feature.tasklist.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.animatedStrikeThrough(
    isCompleted: Boolean,
    textLayoutResult: TextLayoutResult?,
    color: Color = Color.Gray,
    strokeWidth: Dp = 2.dp,
    animationSpec: AnimationSpec<Float> = tween(250)
): Modifier = composed {

    val progress by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        animationSpec = animationSpec,
        label = ""
    )

    drawBehind {

        val layout = textLayoutResult ?: return@drawBehind
        val strokePx = strokeWidth.toPx()

        for (i in 0 until layout.lineCount) {

            val startX = layout.getLineLeft(i)
            val endX = layout.getLineRight(i)
            val width = endX - startX

            val y = layout.getLineTop(i) +
                    (layout.getLineBottom(i) - layout.getLineTop(i)) / 2

            drawLine(
                color = color,
                start = Offset(startX, y),
                end = Offset(startX + width * progress, y),
                strokeWidth = strokePx
            )
        }
    }
}