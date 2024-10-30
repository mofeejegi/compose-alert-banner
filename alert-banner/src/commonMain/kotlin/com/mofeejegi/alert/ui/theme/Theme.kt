package com.mofeejegi.alert.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

/**
 * Dark Mode Composition Local for AlertTheme.
 */
private val LocalIsDarkMode = compositionLocalOf<Boolean> {
    error("LocalIsDarkMode CompositionLocal not set")
}

@Composable
internal fun AlertTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalIsDarkMode provides darkTheme,
    ) {
        val colorScheme = when {
            darkTheme -> MaterialDarkColorScheme
            else -> MaterialLightColorScheme
        }

        MaterialTheme(colorScheme = colorScheme) {
            content()
        }
    }
}

internal object AlertTheme {
    val colorScheme: ColorScheme
        @Composable
        get() = AppColors.getColorScheme(LocalIsDarkMode.current)

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography
}
