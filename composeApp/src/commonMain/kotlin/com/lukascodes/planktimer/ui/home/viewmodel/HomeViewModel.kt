package com.lukascodes.planktimer.ui.home.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.data.prefs.api.observeAsFlow
import com.lukascodes.planktimer.model.StopwatchState.Paused
import com.lukascodes.planktimer.model.StopwatchState.Resumed
import com.lukascodes.planktimer.model.StopwatchState.Stopped
import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.base.uistate.IconDescription
import com.lukascodes.planktimer.ui.base.uistate.IconState
import com.lukascodes.planktimer.ui.base.uistate.toDescription
import com.lukascodes.planktimer.ui.base.viewmodel.BaseViewModel
import com.lukascodes.planktimer.ui.base.viewmodel.updateData
import com.lukascodes.planktimer.ui.home.navigation.HomeDirections
import com.lukascodes.planktimer.ui.home.uistate.StopwatchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(private val dataStore: KeyValueStorageService) : BaseViewModel<HomeState, HomeEvent, HomeDirections>() {
    companion object {
        private const val SCREEN_ID = "home"
    }

    override fun ProducerScope<Unit>.onLifecycleStarted() {
        launch {
            val observedMillisConfig = dataStore.get(KeyValueStorageService::observedTimeMillisConfig)
            val realMillisConfig = dataStore.get(KeyValueStorageService::realTimeMillisConfig)
            val ratio = observedMillisConfig.toDouble() / realMillisConfig
            while (true) {
                val state = dataStore.get(KeyValueStorageService::state)

                var realTime: Duration
                var observedTime: Duration

                when (state) {
                    is Paused -> {
                        realTime = state.pauseTime - state.startTime
                        observedTime = realTime * ratio
                    }
                    is Resumed -> {
                        realTime = Clock.System.now() - state.startTime
                        observedTime = realTime * ratio
                    }
                    Stopped -> {
                        realTime = 0.seconds
                        observedTime = 0.seconds
                    }
                }

                withContext(Dispatchers.Main) {
                    uiState.updateData {
                        HomeState(
                            resetButton = ButtonState.Text(
                                text = "Reset".toDescription(),
                                enabled = state !is Stopped,
                                testId = "reset",
                                screenId = SCREEN_ID,
                            ),
                            settingsButton = ButtonState.Text(
                                text = "Settings".toDescription(),
                                testId = "settings",
                                screenId = SCREEN_ID,
                            ),
                            playPauseButton = ButtonState.Icon(
                                iconState = when (state) {
                                    is Paused -> IconState(
                                        icon = IconDescription.Icon(Icons.Default.PlayArrow),
                                    )
                                    is Resumed -> IconState(
                                        icon = IconDescription.Icon(Icons.Default.Pause),
                                    )
                                    Stopped -> IconState(
                                        icon = IconDescription.Icon(Icons.Default.PlayArrow),
                                    )
                                },
                                "playPause",
                                SCREEN_ID,
                            ),
                            stopwatch = StopwatchState(
                                realTimeDescription = "Real time: ${realMillisConfig.milliseconds.formatToTime()}".toDescription(),
                                realFormattedTime = realTime.formatToTime().toDescription(),
                                realProgress = (realTime.inWholeMilliseconds % 1.minutes.inWholeMilliseconds) / 1.minutes.inWholeMilliseconds.toFloat(),
                                observedFormattedTime = observedTime.formatToTime().toDescription(),
                                observedTimeDescription = "Observed time: ${observedMillisConfig.milliseconds.formatToTime()}".toDescription(),
                                observedProgress = (observedTime.inWholeMilliseconds % 1.minutes.inWholeMilliseconds) / 1.minutes.inWholeMilliseconds.toFloat(),
                                testId = "stopwatch",
                                screenId = SCREEN_ID,
                            )
                        )
                    }
                }
                delay(1.milliseconds)
            }
        }
    }

    override fun onEvent(event: HomeEvent) {
        super.onEvent(event)
        when (event) {
            HomeEvent.PlayPauseToggle -> {
                viewModelScope.launch {
                    dataStore.update(KeyValueStorageService::state) {
                        when (this) {
                            is Paused -> Resumed(this.startTime + (Clock.System.now() - this.pauseTime))
                            is Resumed -> Paused(this.startTime, Clock.System.now())
                            Stopped -> Resumed(Clock.System.now())
                        }
                    }
                }
            }
            HomeEvent.Reset -> {
                viewModelScope.launch {
                    dataStore.set(KeyValueStorageService::state, Stopped)
                }
            }
            HomeEvent.Settings -> {
                navigate(HomeDirections.Settings)
            }
        }
    }

    private fun Duration.formatToTime() = this.toComponents { hours, minutes, seconds, _ ->
        StringBuilder().apply {
            if (hours < 10) {
                append(0)
            }
            append(hours)

            append(":")
            if (minutes < 10) {
                append(0)
            }
            append(minutes)

            append(":")
            if (seconds < 10) {
                append(0)
            }
            append(seconds)
        }.toString()
    }
}
