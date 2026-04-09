package com.mofeejegi.alert.ui.state

import com.mofeejegi.alert.ui.bannertype.AlertBannerType

internal data class AlertBannerViewState(
    val orderedAlerts: List<AlertBannerState> = emptyList(),
)

internal data class AlertBannerState(
    val id: String,
    val message: String,
    val type: AlertBannerType,
    val visible: Boolean = false,
)
