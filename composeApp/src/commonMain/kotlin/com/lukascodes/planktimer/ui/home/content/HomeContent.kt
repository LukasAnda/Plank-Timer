package com.lukascodes.planktimer.ui.home.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TimeInput
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukascodes.planktimer.ui.base.components.IconButton
import com.lukascodes.planktimer.ui.base.components.TextButton
import com.lukascodes.planktimer.ui.home.components.Stopwatch
import com.lukascodes.planktimer.ui.home.viewmodel.HomeEvent
import com.lukascodes.planktimer.ui.home.viewmodel.HomeState
import com.lukascodes.planktimer.ui.settings.components.TimeTicker
import com.lukascodes.planktimer.ui.settings.uistate.TimeTickerState

@Composable
fun HomeContent(
    uiState: HomeState,
    modifier: Modifier = Modifier,
    onEvent: (HomeEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Stopwatch(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            state = uiState.stopwatch,
        )
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                state = uiState.resetButton,
                onClick = {
                    onEvent(HomeEvent.Reset)
                }
            )

            IconButton(
                state = uiState.playPauseButton,
                onClick = {
                    onEvent(HomeEvent.PlayPauseToggle)
                }
            )

            TextButton(
                state = uiState.settingsButton,
                onClick = {
                    onEvent(HomeEvent.Settings)
                }
            )
        }

    }
}