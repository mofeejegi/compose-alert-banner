package com.mofeejegi.alert.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mofeejegi.alert.ui.bannertype.AlertBannerType

/**
 * The colours of each [AlertBannerType]: the banner's container, and the icon and text
 * drawn on it. Create one with [AlertBannerDefaults.colors].
 */
@Immutable
class AlertBannerColors(
    val successContainerColor: Color,
    val successContentColor: Color,
    val errorContainerColor: Color,
    val errorContentColor: Color,
    val infoContainerColor: Color,
    val infoContentColor: Color,
) {
    /** The container colour of a banner of [type]. */
    fun containerColor(type: AlertBannerType): Color = when (type) {
        AlertBannerType.Success -> successContainerColor
        AlertBannerType.Error -> errorContainerColor
        AlertBannerType.Info -> infoContainerColor
    }

    /** The colour of the icon and text on a banner of [type]. */
    fun contentColor(type: AlertBannerType): Color = when (type) {
        AlertBannerType.Success -> successContentColor
        AlertBannerType.Error -> errorContentColor
        AlertBannerType.Info -> infoContentColor
    }

    fun copy(
        successContainerColor: Color = this.successContainerColor,
        successContentColor: Color = this.successContentColor,
        errorContainerColor: Color = this.errorContainerColor,
        errorContentColor: Color = this.errorContentColor,
        infoContainerColor: Color = this.infoContainerColor,
        infoContentColor: Color = this.infoContentColor,
    ): AlertBannerColors = AlertBannerColors(
        successContainerColor = successContainerColor,
        successContentColor = successContentColor,
        errorContainerColor = errorContainerColor,
        errorContentColor = errorContentColor,
        infoContainerColor = infoContainerColor,
        infoContentColor = infoContentColor,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AlertBannerColors) return false
        return successContainerColor == other.successContainerColor &&
            successContentColor == other.successContentColor &&
            errorContainerColor == other.errorContainerColor &&
            errorContentColor == other.errorContentColor &&
            infoContainerColor == other.infoContainerColor &&
            infoContentColor == other.infoContentColor
    }

    override fun hashCode(): Int {
        var result = successContainerColor.hashCode()
        result = 31 * result + successContentColor.hashCode()
        result = 31 * result + errorContainerColor.hashCode()
        result = 31 * result + errorContentColor.hashCode()
        result = 31 * result + infoContainerColor.hashCode()
        result = 31 * result + infoContentColor.hashCode()
        return result
    }
}

object AlertBannerDefaults {
    /**
     * The library's colours for [darkTheme], with any of them replaced. Pass colours read
     * from your own theme to match the banners to your app; they follow the theme when it
     * changes.
     */
    @Composable
    fun colors(
        darkTheme: Boolean = isSystemInDarkTheme(),
        successContainerColor: Color = AppColors.getColorScheme(darkTheme).success,
        successContentColor: Color = AppColors.getColorScheme(darkTheme).tone1,
        errorContainerColor: Color = AppColors.getColorScheme(darkTheme).danger,
        errorContentColor: Color = AppColors.getColorScheme(darkTheme).tone1,
        infoContainerColor: Color = AppColors.getColorScheme(darkTheme).info,
        infoContentColor: Color = AppColors.getColorScheme(darkTheme).tone1,
    ): AlertBannerColors = AlertBannerColors(
        successContainerColor = successContainerColor,
        successContentColor = successContentColor,
        errorContainerColor = errorContainerColor,
        errorContentColor = errorContentColor,
        infoContainerColor = infoContainerColor,
        infoContentColor = infoContentColor,
    )
}
