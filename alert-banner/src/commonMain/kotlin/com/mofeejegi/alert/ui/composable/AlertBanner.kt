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
import com.mofeejegi.alert.ui.theme.AlertBannerColors
import com.mofeejegi.alert.ui.theme.AlertBannerDefaults
import com.mofeejegi.alert.ui.theme.AlertTheme

/**
 * Shows alert banners above [content]. Declare it once at the root of your application;
 * [rememberAlertManager] shows alerts from anywhere inside it.
 *
 * @param darkTheme picks the default colours and the theme the banners are drawn in.
 * @param textStyle the message's style; the theme's `bodySmall` when null.
 * @param contentColor when set, the icon and text colour of every alert type, over [colors].
 * @param colors the container and content colours of each alert type, from
 * [AlertBannerDefaults.colors].
 */
@Composable
fun AlertBanner(
    darkTheme: Boolean = isSystemInDarkTheme(),
    textStyle: TextStyle? = null,
    contentColor: Color? = null,
    colors: AlertBannerColors = AlertBannerDefaults.colors(darkTheme = darkTheme),
    content: @Composable () -> Unit,
) {
    val alertBannerViewModel = viewModel { AlertBannerViewModel() }
    val alertManager = remember { AlertManager(alertBannerViewModel::processEvent) }

    CompositionLocalProvider(LocalAlertManager provides alertManager) {
        content()
    }

    AlertTheme(darkTheme = darkTheme, colors = colors) {
        AlertBannerView(
            textStyle = textStyle ?: AlertTheme.typography.bodySmall,
            onAlertColor = contentColor,
            vm = alertBannerViewModel,
        )
    }
}

@Composable
fun rememberAlertManager(): AlertManager {
    return LocalAlertManager.current
}
