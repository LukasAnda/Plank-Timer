package com.lukascodes.planktimer.ui.settings.navigation

import com.lukascodes.planktimer.ui.base.viewmodel.UiDirection

interface SettingsDirections: UiDirection {
    data object Back: SettingsDirections
}