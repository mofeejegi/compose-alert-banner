package com.mofeejegi.alert.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Set Material Colors
internal val MaterialLightColorScheme = lightColorScheme(
    primary = AppColors.Light.primary,
    secondary = AppColors.Light.secondary,
    background = AppColors.Light.tone1,
    surface = AppColors.Light.tone1,
    onPrimary = AppColors.Light.tone4, // Text
    onSecondary = AppColors.Light.tone4, // Text
    onBackground = AppColors.Light.tone4, // Text
    onSurface = AppColors.Light.tone4, // Text
)

internal val MaterialDarkColorScheme = darkColorScheme(
    primary = AppColors.Dark.primary,
    secondary = AppColors.Dark.secondary,
    background = AppColors.Dark.tone1,
    surface = AppColors.Dark.tone1,
    onPrimary = AppColors.Dark.tone5, // Text
    onSecondary = AppColors.Dark.tone5, // Text
    onBackground = AppColors.Dark.tone5, // Text
    onSurface = AppColors.Dark.tone5, // Text
)

interface ColorScheme {
    // Absolute colours
    val white: Color
    val black: Color
    val gray: Color
    val shadow: Color

    // Variable colours
    val primary: Color
    val secondary: Color
    val tertiary: Color
    val danger: Color
    val success: Color

    val tone0: Color
    val tone1: Color
    val tone2: Color
    val tone3: Color
    val tone4: Color
    val tone5: Color
    val tone6: Color
}

internal object AppColors {
    @Composable
    fun getColorScheme(
        darkMode: Boolean
    ) = if (darkMode) {
        Dark
    } else {
        Light
    }

    @Immutable
    object Light : ColorScheme {
        override val white = Color(0xFF_FFFFFF)
        override val black = Color(0xFF_000F10)
        override val gray = Color(0xFF_757575)
        override val shadow = Color.Black.copy(alpha = 0.3f)

        override val primary = Color(0xFF_018184)
        override val secondary = Color(0xFF_FF8051)
        override val tertiary = Color(0xFF_FFD45D)
        override val danger = Color(0xFF_E53E3E)
        override val success = Color(0xFF_179848)

        override val tone0 = white
        override val tone1 = Color(0xFF_F5F5F5)
        override val tone2 = Color(0xFF_C7C7C7)
        override val tone3 = gray
        override val tone4 = Color(0xFF_383838)
        override val tone5 = Color(0xFF_0A0A0A)
        override val tone6 = black
    }

    @Immutable
    object Dark : ColorScheme {
        override val white = Color(0xFF_FFFFFF)
        override val black = Color(0xFF_000F10)
        override val gray = Color(0xFF_757575)
        override val shadow = Color.Unspecified // No shadow in dark mode

        override val primary = Color(0xFF_002E2F)
        override val secondary = Color(0xFF_E1582D)
        override val tertiary = Color(0xFF_FFB300)
        override val danger = Color(0xFF_FFB3B3)
        override val success = Color(0xFF_B9F5D0)

        override val tone0 = black
        override val tone1 = Color(0xFF_0A0A0A)
        override val tone2 = Color(0xFF_383838)
        override val tone3 = gray
        override val tone4 = Color(0xFF_C7C7C7)
        override val tone5 = Color(0xFF_F5F5F5)
        override val tone6 = white
    }
}
