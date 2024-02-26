package com.lukascodes.planktimer.ui.nav

import kotlinx.serialization.Serializable
import tech.annexflow.precompose.navigation.typesafe.Route

sealed interface AppRoutes : Route {
    @Serializable
    data object Home : AppRoutes

    @Serializable
    data object Settings : AppRoutes
}