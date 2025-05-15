package com.example.aplikacjabarmanska.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.aplikacjabarmanska.data.ThemeManager

// Kolory dla trybu jasnego
val LightPrimary = Color(0xFF6650a4)
val LightSecondary = Color(0xFF625b71)
val LightTertiary = Color(0xFF7D5260)
val LightBackground = Color(0xFFE6E2DC)
val LightSurface = Color(0xFFC6BBAE)
val LightSurfaceContainer = Color(0xFFE0D6C9)
val LightAppBarColor = Color(0xFF8C7F6A)

// Kolory dla trybu ciemnego
val DarkPrimary = Color(0xFFD0BCFF)
val DarkSecondary = Color(0xFFCCC2DC)
val DarkTertiary = Color(0xFFEFB8C8)
val DarkBackground = Color(0xFF1C1B1F)
val DarkSurface = Color(0xFF2B292F)
val DarkSurfaceContainer = Color(0xFF3D3D3D)
val DarkAppBarColor = Color(0xFF333333)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,

    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,

    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun AplikacjaBarmanskaTheme(
    // Nie używamy już domyślnego parametru z isSystemInDarkTheme()
    // darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Używamy ThemeManager do kontrolowania trybu ciemnego
    val themeManager = ThemeManager.getInstance()
    val isDarkTheme by themeManager.isDarkTheme.collectAsState()

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}