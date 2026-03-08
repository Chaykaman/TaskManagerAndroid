package com.example.taskmanager.ui.theme

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
    primary              = Primary80,
    onPrimary            = Primary20,
    primaryContainer     = Primary30,
    onPrimaryContainer   = Primary90,
    inversePrimary       = Primary40,

    secondary            = Secondary80,
    onSecondary          = Secondary20,
    secondaryContainer   = Secondary30,
    onSecondaryContainer = Secondary90,

    error                = Error80,
    onError              = Error20,
    errorContainer       = Error30,
    onErrorContainer     = Error90,

    background           = DarkBackground,
    onBackground         = Neutral90,

    surface              = DarkSurface,
    onSurface            = Neutral90,
    inverseSurface       = Neutral90,
    inverseOnSurface     = Neutral20,

    surfaceVariant       = DarkSurfaceVariant,
    onSurfaceVariant     = DarkOnSurfaceVariant,
    surfaceContainer     = DarkSurfaceContainer,
    surfaceContainerLow  = DarkBackground,
    surfaceContainerHigh = DarkBackground,
    surfaceContainerHighest = DarkSurfaceContainer,

    outline              = DarkOutline,
)

private val LightColorScheme = lightColorScheme(
    primary              = Primary40,
    onPrimary            = Color.White,
    primaryContainer     = Primary90,
    onPrimaryContainer   = Primary10,
    inversePrimary       = Primary80,

    secondary            = Secondary40,
    onSecondary          = Color.White,
    secondaryContainer   = Secondary90,
    onSecondaryContainer = Secondary30,

    error                = Error40,
    onError              = Color.White,
    errorContainer       = Error90,
    onErrorContainer     = Error10,

    background           = LightBackground,
    onBackground         = Neutral10,

    surface              = LightBackground,
    onSurface            = Neutral10,
    inverseSurface       = Neutral20,
    inverseOnSurface     = Neutral95,

    surfaceVariant       = LightSurfaceVariant,
    onSurfaceVariant     = LightOnSurfaceVariant,
    surfaceContainer     = LightSurfaceContainer,
    surfaceContainerLow  = LightBackground,
    surfaceContainerHigh = LightBackground,
    surfaceContainerHighest = LightSurfaceContainer,

    outline              = LightOutline,
)

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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