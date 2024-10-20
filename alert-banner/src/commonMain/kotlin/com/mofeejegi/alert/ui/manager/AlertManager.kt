package com.mofeejegi.alert.ui.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import com.mofeejegi.alert.ui.bannertype.AlertBannerType
import com.mofeejegi.alert.ui.state.AlertBannerViewEvent
import com.mofeejegi.alert.ui.state.AlertShown
import kotlinx.datetime.Clock

class AlertManager internal constructor(private val processEvent: (AlertBannerViewEvent) -> Unit) {
    fun show(message: String, type: AlertBannerType) {
        processEvent(
            AlertShown(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                message = message,
                type = type,
            )
        )
    }
}

object LocalAlertManager {
    private val LocalAlertManager = compositionLocalOf<AlertManager> {
        error("LocalAlertManager not set")
    }

    val current: AlertManager
        @Composable get() = if (LocalInspectionMode.current) {
            AlertManager {}
        } else {
            LocalAlertManager.current
        }

    infix fun provides(
        alertManager: AlertManager,
    ): ProvidedValue<AlertManager> {
        return LocalAlertManager provides alertManager
    }
}
