package com.lukascodes.planktimer.ui.home.navigation

import com.lukascodes.planktimer.ui.base.viewmodel.UiDirection

sealed interface HomeDirections : UiDirection {
    data object Settings: HomeDirections
}