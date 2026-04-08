package com.mofeejegi.alert.sample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("Alert Banner Sample") {
        SampleApp()
    }
}
