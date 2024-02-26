package com.lukascodes.planktimer.ui.settings.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lukascodes.planktimer.ui.base.viewmodel.UiState
import com.lukascodes.planktimer.ui.settings.content.SettingsContent
import com.lukascodes.planktimer.ui.settings.viewmodel.SettingsViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject

@Composable
fun SettingsDestination(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinInject<SettingsViewModel>()
    val uiState by viewModel.state.collectAsStateWithLifecycle(UiState(null))

    uiState.data?.let {
        SettingsContent(
            uiState = it,
            modifier = modifier.fillMaxSize(),
            onEvent = viewModel::onEvent,
        )
    }

    val directions by viewModel.direction.collectAsStateWithLifecycle(null)
    LaunchedEffect(directions) {
        when (directions) {
            SettingsDirections.Back -> {
                navigator.goBack()
            }
            null -> {}
        }
    }
}