package com.lukascodes.planktimer.ui.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lukascodes.planktimer.ui.base.viewmodel.UiState
import com.lukascodes.planktimer.ui.home.content.HomeContent
import com.lukascodes.planktimer.ui.home.viewmodel.HomeViewModel
import com.lukascodes.planktimer.ui.nav.AppRoutes
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import tech.annexflow.precompose.navigation.typesafe.generateRoutePattern
import tech.annexflow.precompose.navigation.typesafe.navigate

@Composable
fun HomeDestination(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinInject<HomeViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle(UiState(null))

    uiState.data?.let {
        HomeContent(
            uiState = it,
            modifier = modifier.fillMaxSize(),
            onEvent = viewModel::onEvent,
        )
    }

    val directions by viewModel.direction.collectAsStateWithLifecycle(null)
    LaunchedEffect(directions) {
        when (directions) {
            HomeDirections.Settings -> {
                navigator.navigate(AppRoutes.Settings)
            }
            null -> {}
        }
    }
}