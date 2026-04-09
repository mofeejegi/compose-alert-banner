package com.mofeejegi.alert.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AlertBannerViewModel : ViewModel() {

    private var _viewState = MutableStateFlow(AlertBannerViewState())
    val viewState: StateFlow<AlertBannerViewState>
        get() = _viewState.asStateFlow()

    private val alerts: MutableMap<String, AlertBannerState> = mutableMapOf()

    fun processEvent(event: AlertBannerViewEvent) {
        when (event) {
            is AlertShown -> {
                val counter = alerts.size
                val compositeId = "${event.id}_${counter}" // Prevent collisions for rapid addition

                alerts[compositeId] = AlertBannerState(compositeId, event.message, event.type)
                sortAlerts()
            }

            is AlertAnimatedIn -> {
                alerts[event.id]?.let {
                    alerts[event.id] = it.copy(visible = true)
                    sortAlerts()
                }
            }

            is AlertAnimatedOut -> {
                alerts[event.id]?.let {
                    alerts[event.id] = it.copy(visible = false)
                    sortAlerts()
                }
            }

            is AlertDismissed -> {
                alerts.remove(event.id)
                sortAlerts()
            }
        }
    }

    private fun sortAlerts() {
        _viewState.value = _viewState.value.copy(
            orderedAlerts = alerts.entries.sortedByDescending { it.key }.map { it.value }
        )
    }
}
