package com.jetli.vina.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.draw.alpha
import android.view.MotionEvent
import androidx.compose.ui.ExperimentalComposeUiApi

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TouchableOpacity(
    onClick:   () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .alpha(if (isPressed) 0.1f else 1f) // Adjusting opacity based on press state
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed = true // Button is pressed
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        isPressed = false // Button released
                        onClick() // Handle the click action
                        true
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        isPressed = false // Handle cancellation
                        true
                    }

                    else -> false
                }
            }
    ) {
        content() // Display the nested content
    }
}

