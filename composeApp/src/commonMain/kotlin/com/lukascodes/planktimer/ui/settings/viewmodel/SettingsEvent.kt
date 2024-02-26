package com.lukascodes.planktimer.ui.settings.viewmodel

import com.lukascodes.planktimer.ui.base.viewmodel.UiEvent
import kotlin.time.Duration

sealed interface SettingsEvent : UiEvent {
    data object Save : SettingsEvent
    data object Back : SettingsEvent
    data class RealTimeSettings(val duration: Duration) : SettingsEvent
    data class ObservedTimeSettings(val duration: Duration) : SettingsEvent
}