package com.lukascodes.planktimer.ui.settings.viewmodel

import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.base.uistate.StringDescription
import com.lukascodes.planktimer.ui.settings.uistate.TimeTickerState

data class SettingsState(
    val observedTimeTickerState: TimeTickerState,
    val observedTimeDescription: StringDescription,
    val realTimeTickerState: TimeTickerState,
    val realTimeDescription: StringDescription,
    val saveButton: ButtonState.Text,
    val backButton: ButtonState.Text,
)