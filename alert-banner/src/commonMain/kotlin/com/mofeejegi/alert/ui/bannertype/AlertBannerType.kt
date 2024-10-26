package com.mofeejegi.alert.ui.bannertype

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mofeejegi.alert.alert_banner.generated.resources.Res
import com.mofeejegi.alert.alert_banner.generated.resources.ic_check_circle
import com.mofeejegi.alert.alert_banner.generated.resources.ic_error
import com.mofeejegi.alert.ui.theme.AlertTheme
import org.jetbrains.compose.resources.DrawableResource

sealed class AlertBannerType {
    @get:Composable
    abstract val color: Color
    abstract val icon: DrawableResource

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
}
