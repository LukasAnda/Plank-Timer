package com.lukascodes.planktimer.ui.home.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukascodes.planktimer.ui.base.components.IconButton
import com.lukascodes.planktimer.ui.base.components.TextButton
import com.lukascodes.planktimer.ui.base.viewmodel.UiState
import com.lukascodes.planktimer.ui.home.components.Stopwatch
import com.lukascodes.planktimer.ui.home.viewmodel.HomeEvent
import com.lukascodes.planktimer.ui.home.viewmodel.HomeState

@Composable
fun HomeContent(
    uiState: UiState<out HomeState?>,
    modifier: Modifier = Modifier,
    onEvent: (HomeEvent) -> Unit,
) {
    val homeState = uiState.data ?: return

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Stopwatch(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            state = homeState.stopwatch,
        ) {
            onEvent(HomeEvent.StopWatchPageSelected(it))
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                state = homeState.resetButton,
                onClick = {
                    onEvent(HomeEvent.Reset)
                }
            )

            IconButton(
                state = homeState.playPauseButton,
                onClick = {
                    onEvent(HomeEvent.PlayPauseToggle)
                }
            )

            TextButton(
                state = homeState.settingsButton,
                onClick = {
                    onEvent(HomeEvent.Settings)
                }
            )
        }

    }
}