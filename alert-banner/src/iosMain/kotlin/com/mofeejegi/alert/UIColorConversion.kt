package com.mofeejegi.alert

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloatVar
import platform.UIKit.UIColor

/**
 * Converts this color to a Compose [Color].
 *
 * UIKit reports components in extended sRGB, which goes below 0 and above 1 for wide-gamut
 * colors such as Display P3, so the result uses Compose's extended sRGB color space too.
 * Dynamic colors like `.systemPurple` resolve for the current appearance.
 * Colors with no RGB components, such as pattern-image colors, become [Color.Unspecified].
 */
@OptIn(ExperimentalForeignApi::class)
internal fun UIColor.toComposeColor(): Color = memScoped {
    val red = alloc<CGFloatVar>()
    val green = alloc<CGFloatVar>()
    val blue = alloc<CGFloatVar>()
    val alpha = alloc<CGFloatVar>()
    if (!getRed(red.ptr, green = green.ptr, blue = blue.ptr, alpha = alpha.ptr)) {
        return@memScoped Color.Unspecified
    }

    val space = ColorSpaces.ExtendedSrgb
    fun channel(value: Double, index: Int) =
        value.toFloat().coerceIn(space.getMinValue(index), space.getMaxValue(index))

    Color(
        red = channel(red.value, 0),
        green = channel(green.value, 1),
        blue = channel(blue.value, 2),
        alpha = alpha.value.toFloat().coerceIn(0f, 1f),
        colorSpace = space,
    )
}
