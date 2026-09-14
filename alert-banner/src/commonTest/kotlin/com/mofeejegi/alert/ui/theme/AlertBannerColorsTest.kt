package com.mofeejegi.alert.ui.theme

import androidx.compose.ui.graphics.Color
import com.mofeejegi.alert.ui.bannertype.AlertBannerType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AlertBannerColorsTest {

    private val colors = AlertBannerColors(
        successContainerColor = Color(0xFF_000001),
        successContentColor = Color(0xFF_000002),
        errorContainerColor = Color(0xFF_000003),
        errorContentColor = Color(0xFF_000004),
        infoContainerColor = Color(0xFF_000005),
        infoContentColor = Color(0xFF_000006),
    )

    @Test
    fun eachTypeReadsItsOwnContainerColor() {
        assertEquals(Color(0xFF_000001), colors.containerColor(AlertBannerType.Success))
        assertEquals(Color(0xFF_000003), colors.containerColor(AlertBannerType.Error))
        assertEquals(Color(0xFF_000005), colors.containerColor(AlertBannerType.Info))
    }

    @Test
    fun eachTypeReadsItsOwnContentColor() {
        assertEquals(Color(0xFF_000002), colors.contentColor(AlertBannerType.Success))
        assertEquals(Color(0xFF_000004), colors.contentColor(AlertBannerType.Error))
        assertEquals(Color(0xFF_000006), colors.contentColor(AlertBannerType.Info))
    }

    @Test
    fun copyReplacesOnlyTheColorsItNames() {
        val copied = colors.copy(infoContainerColor = Color.Red)

        assertEquals(Color.Red, copied.infoContainerColor)
        assertEquals(colors.infoContentColor, copied.infoContentColor)
        assertEquals(colors.successContainerColor, copied.successContainerColor)
        assertEquals(colors.errorContainerColor, copied.errorContainerColor)
    }

    @Test
    fun equalityFollowsTheColors() {
        assertEquals(colors, colors.copy())
        assertEquals(colors.hashCode(), colors.copy().hashCode())
        assertNotEquals(colors, colors.copy(errorContentColor = Color.Red))
    }
}
