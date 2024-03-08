package com.lukascodes.planktimer.ui.home.viewmodel

import com.lukascodes.planktimer.ui.base.viewmodel.UiEvent

sealed interface HomeEvent : UiEvent {
    data object PlayPauseToggle : HomeEvent
    data object Reset : HomeEvent
    data object Settings : HomeEvent
    data class StopWatchPageSelected(val page: Int): HomeEvent
}