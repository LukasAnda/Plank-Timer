package com.lukascodes.planktimer.ui.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lukascodes.planktimer.ui.base.viewmodel.UiState
import com.lukascodes.planktimer.ui.home.components.KeepScreenOn
import com.lukascodes.planktimer.ui.home.content.HomeContent
import com.lukascodes.planktimer.ui.home.viewmodel.HomeViewModel
import com.lukascodes.planktimer.ui.nav.AppRoutes
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import tech.annexflow.precompose.navigation.typesafe.navigate

@Composable
fun HomeDestination(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinInject<HomeViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle(UiState(null))
    val directions by viewModel.direction.collectAsStateWithLifecycle(null)

    if (uiState.keepScreenOn) {
        KeepScreenOn()
    }

    HomeContent(
        uiState = uiState,
        modifier = modifier.fillMaxSize(),
        onEvent = viewModel::onEvent,
    )

    HomeNavigation(
        directions = directions,
        navigator = navigator,
    )
}

@Composable
private fun HomeNavigation(directions: HomeDirections?, navigator: Navigator) {
    LaunchedEffect(directions) {
        when (directions) {
            HomeDirections.Settings -> {
                navigator.navigate(AppRoutes.Settings)
            }
            null -> {}
        }
    }
}