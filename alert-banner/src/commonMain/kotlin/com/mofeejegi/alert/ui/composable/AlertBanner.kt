package com.mofeejegi.alert.ui.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mofeejegi.alert.ui.manager.AlertManager
import com.mofeejegi.alert.ui.manager.LocalAlertManager
import com.mofeejegi.alert.ui.state.AlertBannerViewModel
import com.mofeejegi.alert.ui.theme.AlertTheme

@Composable
fun AlertBanner(
    darkTheme: Boolean = isSystemInDarkTheme(),
    textStyle: TextStyle? = null,
    contentColor: Color? = null,
    content: @Composable () -> Unit,
) {
    val alertBannerViewModel = viewModel { AlertBannerViewModel() }
    val alertManager = remember { AlertManager(alertBannerViewModel::processEvent) }

    CompositionLocalProvider(LocalAlertManager provides alertManager) {
        content()
    }

    AlertTheme(darkTheme = darkTheme) {
        AlertBannerView(
            textStyle = textStyle ?: AlertTheme.typography.bodySmall,
            onAlertColor = contentColor ?: AlertTheme.colorScheme.tone1,
            vm = alertBannerViewModel,
        )
    }
}

@Composable
fun rememberAlertManager(): AlertManager {
    return LocalAlertManager.current
}
