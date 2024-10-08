package com.jetli.vina.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressionAnimation() {
    // Define the animated progress state
    var progress by remember { mutableStateOf(0f) }

    // Define an infinite transition for continuous progress
    val infiniteTransition = rememberInfiniteTransition()

    // Animating the progress from 0 to 1 in a loop
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Update the progress state
    progress = animatedProgress

    // Canvas to draw the circular progress
    Canvas(modifier = Modifier.size(100.dp)) {
        // Define the size of the circle
        val size = size.minDimension
        val strokeWidth = 8.dp.toPx()
        val arcSize = Size(size - strokeWidth, size - strokeWidth)

        // Draw the circular track
        drawArc(
            color = Color.Gray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = arcSize,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        // Draw the animated circular progress
        drawArc(
            color = Color.Cyan,
            startAngle = -90f, // Start from top
            sweepAngle = progress * 360f, // Progress to 360 degrees
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = arcSize,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircularProgression() {
    CircularProgressionAnimation()
}
