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
    /** This type's container colour, from the colours given to `AlertBanner`. */
    @get:Composable
    abstract val color: Color
    abstract val icon: DrawableResource

    data object Error : AlertBannerType() {
        override val color: Color
            @Composable get() = AlertTheme.colors.errorContainerColor

        override val icon: DrawableResource = Res.drawable.ic_error
    }

    data object Success : AlertBannerType() {
        override val color: Color
            @Composable get() = AlertTheme.colors.successContainerColor

        override val icon: DrawableResource = Res.drawable.ic_check_circle
    }

    data object Info : AlertBannerType() {
        override val color: Color
            @Composable get() = AlertTheme.colors.infoContainerColor

        override val icon: DrawableResource = Res.drawable.ic_info
    }
}
