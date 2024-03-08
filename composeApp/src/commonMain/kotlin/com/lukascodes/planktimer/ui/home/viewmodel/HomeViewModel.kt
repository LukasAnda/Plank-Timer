package com.lukascodes.planktimer.ui.home.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.model.StopwatchState.Paused
import com.lukascodes.planktimer.model.StopwatchState.Resumed
import com.lukascodes.planktimer.model.StopwatchState.Stopped
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent.StopwatchAction
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import com.lukascodes.planktimer.services.analytics.logEvent
import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.base.uistate.IconDescription
import com.lukascodes.planktimer.ui.base.uistate.IconState
import com.lukascodes.planktimer.ui.base.uistate.StringDescription
import com.lukascodes.planktimer.ui.base.uistate.toDescription
import com.lukascodes.planktimer.ui.base.viewmodel.BaseViewModel
import com.lukascodes.planktimer.ui.base.viewmodel.updateData
import com.lukascodes.planktimer.ui.home.navigation.HomeDirections
import com.lukascodes.planktimer.ui.home.uistate.StopwatchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import moe.tlaster.precompose.viewmodel.viewModelScope
import net.sergeych.sprintf.sprintf
import plank_timer.composeapp.generated.resources.Res
import plank_timer.composeapp.generated.resources.observed_time_description
import plank_timer.composeapp.generated.resources.real_time_description
import plank_timer.composeapp.generated.resources.reset
import plank_timer.composeapp.generated.resources.settings
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(private val dataStore: KeyValueStorageService, analyticsProvider: AnalyticsProvider) : BaseViewModel<HomeState, HomeEvent, HomeDirections>(analyticsProvider) {
    companion object {
        private const val SCREEN_ID = "home"
    }

    override val screenView: String
        get() = "Home"

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
                                text = Res.string.reset.toDescription(),
                                enabled = state !is Stopped,
                                testId = "reset",
                                screenId = SCREEN_ID,
                            ),
                            settingsButton = ButtonState.Text(
                                text = Res.string.settings.toDescription(),
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
                                realTimeDescription = StringDescription.Resource(Res.string.real_time_description, realMillisConfig.milliseconds.formatToTime()),
                                realFormattedTime = realTime.formatToTime().toDescription(),
                                realProgress = (realTime.inWholeMilliseconds % 1.minutes.inWholeMilliseconds) / 1.minutes.inWholeMilliseconds.toFloat(),
                                observedFormattedTime = observedTime.formatToTime().toDescription(),
                                observedTimeDescription = StringDescription.Resource(Res.string.observed_time_description, observedMillisConfig.milliseconds.formatToTime()),
                                observedProgress = (observedTime.inWholeMilliseconds % 1.minutes.inWholeMilliseconds) / 1.minutes.inWholeMilliseconds.toFloat(),
                                testId = "stopwatch",
                                screenId = SCREEN_ID,
                            )
                        )
                    }
                    uiState.update { it.copy(keepScreenOn = state is Resumed) }
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
                            is Paused -> {
                                val newStartTime = this.startTime + (Clock.System.now() - this.pauseTime)
                                analyticsProvider.logEvent(
                                    StopwatchAction.Resume(
                                        (Clock.System.now() - newStartTime).inWholeSeconds.toInt()
                                    )
                                )
                                Resumed(newStartTime)
                            }
                            is Resumed -> {
                                analyticsProvider.logEvent(
                                    StopwatchAction.Stop(
                                        (Clock.System.now() - this.startTime).inWholeSeconds.toInt()
                                    )
                                )
                                Paused(this.startTime, Clock.System.now())
                            }
                            Stopped -> {
                                analyticsProvider.logEvent(StopwatchAction.Start)
                                Resumed(Clock.System.now())
                            }
                        }
                    }
                }
            }
            HomeEvent.Reset -> {
                viewModelScope.launch {

                    val passedSeconds = when (val currentState = dataStore.get(KeyValueStorageService::state)) {
                        is Paused -> Clock.System.now() - currentState.startTime
                        is Resumed -> Clock.System.now() - currentState.startTime
                        Stopped -> 0.seconds
                    }
                    analyticsProvider.logEvent(
                        StopwatchAction.Reset(passedSeconds.inWholeSeconds.toInt())
                    )

                    dataStore.set(KeyValueStorageService::state, Stopped)
                }
            }
            HomeEvent.Settings -> {
                analyticsProvider.logEvent(StopwatchAction.Start)
                navigate(HomeDirections.Settings)
            }
        }
    }

    private fun Duration.formatToTime() = this.toComponents { hours, minutes, seconds, _ ->
        "%02d:%02d:%02d".sprintf(hours, minutes, seconds)
    }
}
