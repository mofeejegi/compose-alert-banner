package com.mofeejegi.alert.ui.bannertype

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mofeejegi.alert.alert_banner.generated.resources.Res
import com.mofeejegi.alert.alert_banner.generated.resources.ic_check_circle
import com.mofeejegi.alert.alert_banner.generated.resources.ic_error
import com.mofeejegi.alert.alert_banner.generated.resources.ic_info
import com.mofeejegi.alert.ui.theme.AlertTheme
import org.jetbrains.compose.resources.DrawableResource

sealed class AlertBannerType {
    @get:Composable
    abstract val color: Color
    abstract val icon: DrawableResource?

    data object Error : AlertBannerType() {
        override val color: Color
            @Composable get() = AlertTheme.colorScheme.danger

        override val icon: DrawableResource = Res.drawable.ic_error
    }

    data object Success : AlertBannerType() {
        override val color: Color
            @Composable get() = AlertTheme.colorScheme.success

        override val icon: DrawableResource = Res.drawable.ic_check_circle
    }

    /**
     * An alert in colors of your choosing: [containerColor] behind it, [contentColor] for
     * its icon and text. The only type whose colors can be set. Its icon is the info glyph,
     * or none when [showIcon] is false.
     */
    data class Custom(
        val containerColor: Color,
        val contentColor: Color,
        val showIcon: Boolean = true,
    ) : AlertBannerType() {
        override val color: Color
            @Composable get() = containerColor

        override val icon: DrawableResource? = if (showIcon) Res.drawable.ic_info else null
    }
}
