package com.mofeejegi.alert.ui.state

import com.mofeejegi.alert.ui.bannertype.AlertBannerType

internal sealed interface AlertBannerViewEvent

internal data class AlertShown(
    val id: String,
    val message: String,
    val type: AlertBannerType,
) : AlertBannerViewEvent

internal data class AlertAnimatedIn(val id: String) : AlertBannerViewEvent

internal data class AlertAnimatedOut(val id: String) : AlertBannerViewEvent

internal data class AlertDismissed(val id: String) : AlertBannerViewEvent
