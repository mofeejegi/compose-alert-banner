package com.mofeejegi.alert.ui.state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.mofeejegi.alert.ui.bannertype.AlertBannerType

internal data class AlertBannerViewState(
    val alerts: SnapshotStateMap<String, AlertBannerState> = mutableStateMapOf()
) {
    fun orderedAlerts(): List<AlertBannerState> {
        return alerts.toList().sortedByDescending { it.first }.map { it.second }
    }
}

internal data class AlertBannerState(
    val id: String,
    val message: String,
    val type: AlertBannerType,
    val visible: Boolean = false,
)
