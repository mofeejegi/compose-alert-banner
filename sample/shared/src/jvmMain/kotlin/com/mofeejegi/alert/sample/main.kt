package com.mofeejegi.alert.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Alert Banner Sample",
    ) {
        SampleApp()
    }
}
