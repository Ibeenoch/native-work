package com.jetli.vina.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Define your light and dark color schemes
private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF000E28), // Hex color #000e28 for dark mode background
    onBackground = Color.White      // White text on dark background
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFF7F7F7), // Hex color #f7f7f7 for light mode background
    onBackground = Color.Black      // Black text on light background
)

// Define your custom theme composable
@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detect system theme
    content: @Composable () -> Unit
) {
    // Choose the color scheme based on the darkTheme flag
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    // Apply the theme using MaterialTheme
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            bodyLarge = TextStyle( // Override the body text style if needed
                fontSize = 16.sp,
                color = if (darkTheme) Color.White else Color.Black // White on dark, black on light
            )
        ),
        content = content // Apply the theme to the content
    )
}

//@Composable
//fun MyApp() {
//    // Wrap the entire app in your custom theme
//    CustomTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background // Apply background color globally
//        ) {
//            // Your app content goes here
//            Text(
//                text = "Hello World",
//                style = MaterialTheme.typography.bodyLarge, // Text style adapts based on the theme
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}
