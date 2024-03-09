@file:OptIn(ExperimentalFoundationApi::class)

package com.lukascodes.planktimer.ui.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.lukascodes.planktimer.ui.home.uistate.StopwatchState

@Composable
fun Stopwatch(state: StopwatchState, modifier: Modifier = Modifier, onPageSelected: (Int) -> Unit = {}) {
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
        WatchFacePager(state = state, onPageSelected = onPageSelected)
    }
}

@Composable
private fun BoxScope.WatchFacePager(state: StopwatchState, modifier: Modifier = Modifier, onPageSelected: (Int) -> Unit = {}) {
    val pageState = rememberPagerState(state.defaultPage) { 3 }
    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            onPageSelected(page)
        }
    }
    HorizontalPager(
        state = pageState,
        modifier = modifier
            .align(Alignment.Center)
            .fillMaxSize()
            .padding(36.dp)
            .clip(CircleShape),
    ) { itemIndex ->
        when (itemIndex) {
            0 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 4.dp),
                            text = state.observedTimeDescription.text(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = state.observedFormattedTime.text(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            1 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 4.dp),
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
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 4.dp),
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
            2 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 4.dp),
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
        }
    }

    WormPageIndicator(
        pagerState = pageState,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)
    )
}