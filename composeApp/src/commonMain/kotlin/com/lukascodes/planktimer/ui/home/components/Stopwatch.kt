package com.lukascodes.planktimer.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lukascodes.planktimer.ui.home.uistate.StopwatchState

@Composable
fun Stopwatch(state: StopwatchState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .testTag(state.testTag),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { state.observedProgress },
            strokeCap = StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.secondaryContainer,
            strokeWidth = 16.dp,
        )
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize().padding(18.dp),
            progress = { state.realProgress },
            strokeCap = StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            strokeWidth = 16.dp,
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = state.observedTimeDescription.text(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = state.observedFormattedTime.text(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = state.realTimeDescription.text(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = state.realFormattedTime.text(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}