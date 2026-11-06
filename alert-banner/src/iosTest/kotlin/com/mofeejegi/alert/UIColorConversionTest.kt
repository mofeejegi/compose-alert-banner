package com.mofeejegi.alert

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import platform.UIKit.UIColor
import platform.UIKit.systemPurpleColor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class UIColorConversionTest {

    private fun assertSrgb(expected: Color, actual: Color) {
        val converted = actual.convert(ColorSpaces.Srgb)
        assertEquals(expected.red, converted.red, 0.01f, "red")
        assertEquals(expected.green, converted.green, 0.01f, "green")
        assertEquals(expected.blue, converted.blue, 0.01f, "blue")
        assertEquals(expected.alpha, converted.alpha, 0.01f, "alpha")
    }

    @Test
    fun basicColorsConvert() {
        assertSrgb(Color.Red, UIColor.redColor.toComposeColor())
        assertSrgb(Color.White, UIColor.whiteColor.toComposeColor())
        assertSrgb(Color.Black, UIColor.blackColor.toComposeColor())
    }

    @Test
    fun componentsAndAlphaArePreserved() {
        val color = UIColor.colorWithRed(0.2, green = 0.4, blue = 0.6, alpha = 0.5).toComposeColor()
        assertSrgb(Color(0.2f, 0.4f, 0.6f, 0.5f), color)
    }

    @Test
    fun wideGamutColorDoesNotCrash() {
        // Display P3 red is outside sRGB, so UIKit reports a red component above 1.
        val color = UIColor.colorWithDisplayP3Red(1.0, green = 0.0, blue = 0.0, alpha = 1.0).toComposeColor()
        assertTrue(color.red > 1f, "expected an extended sRGB red above 1, got ${color.red}")
        assertEquals(1f, color.alpha)
    }

    @Test
    fun dynamicSystemColorResolves() {
        assertNotEquals(Color.Unspecified, UIColor.systemPurpleColor.toComposeColor())
    }
}
