package com.mofeejegi.alert.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AlertBannerViewModel : ViewModel() {

    private var _viewState = MutableStateFlow(AlertBannerViewState())
    val viewState: StateFlow<AlertBannerViewState>
        get() = _viewState.asStateFlow()

    fun processEvent(event: AlertBannerViewEvent) {
        when (event) {
            is AlertShown -> {
                val counter = _viewState.value.alerts.size
                val compositeId = "${event.id}_${counter}" // Prevent collisions for rapid addition

                val alert = AlertBannerState(compositeId, event.message, event.type)
                _viewState.value.alerts[compositeId] = alert
            }

            is AlertAnimatedIn -> {
                val alert = _viewState.value.alerts[event.id]
                alert?.let {
                    _viewState.value.alerts[alert.id] = alert.copy(visible = true)
                }
            }

            is AlertAnimatedOut -> {
                val alert = _viewState.value.alerts[event.id]
                alert?.let {
                    _viewState.value.alerts[alert.id] = it.copy(visible = false)
                }
            }

            is AlertDismissed -> {
                _viewState.value.alerts.remove(event.id)
            }
        }
    }
}
