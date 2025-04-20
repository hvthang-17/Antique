package com.example.antique.view.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White, //pages
    surface = Color.White, //surface login mobile

    onPrimary = Color(219, 229, 250), //anything (e.g Text) on a component that uses primary color
    onSecondary = Color(0xFF000000), // 242B3B
    onTertiary = Color(0xFFE91E63),
    onSurfaceVariant = Color(0xFF000000), //text in cards that do not use the defailt color

    onBackground = Color(0xFF1C1B1F),
//onSurface = Color(0xFF2900A5),
    secondaryContainer = Color(0xFFB1C7FF), //top bar and selected bottom bar and filter chips background
    onPrimaryContainer = Color(0xFF000000), //
    primaryContainer = Color(0xFFadc7ff),
    onSecondaryContainer = Color(0xFF000000),
    surfaceVariant = Color(0xFFB1C7FF) // card color
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AntiqueTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}