package com.lukascodes.planktimer.ui.home.viewmodel

import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.home.uistate.StopwatchState

data class HomeState(
    val resetButton: ButtonState.Text,
    val settingsButton: ButtonState.Text,
    val playPauseButton: ButtonState.Icon,
    val stopwatch: StopwatchState,
)