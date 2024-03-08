package com.lukascodes.planktimer.ui.home.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.extensions.formatToTime
import com.lukascodes.planktimer.model.StopwatchState.Paused
import com.lukascodes.planktimer.model.StopwatchState.Resumed
import com.lukascodes.planktimer.model.StopwatchState.Stopped
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent.StopwatchAction
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import com.lukascodes.planktimer.services.analytics.logEvent
import com.lukascodes.planktimer.services.stopwatch.StopwatchService
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
import moe.tlaster.precompose.viewmodel.viewModelScope
import plank_timer.composeapp.generated.resources.Res
import plank_timer.composeapp.generated.resources.observed_time_description
import plank_timer.composeapp.generated.resources.real_time_description
import plank_timer.composeapp.generated.resources.reset
import plank_timer.composeapp.generated.resources.settings
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class HomeViewModel(
    private val dataStore: KeyValueStorageService,
    analyticsProvider: AnalyticsProvider,
    private val stopwatchService: StopwatchService,
) : BaseViewModel<HomeState, HomeEvent, HomeDirections>(analyticsProvider) {
    companion object {
        private const val SCREEN_ID = "home"
    }

    override val screenView: String
        get() = "Home"

    override fun ProducerScope<Unit>.onLifecycleStarted() {
        launch {
            val observedMillisConfig = dataStore.get(KeyValueStorageService::observedTimeMillisConfig)
            val realMillisConfig = dataStore.get(KeyValueStorageService::realTimeMillisConfig)

            while (true) {
                val state = dataStore.get(KeyValueStorageService::state)

                val (realTime, observedTime) = stopwatchService.getStopwatchTimes()

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
                    stopwatchService.togglePlayPause()
                }
            }
            HomeEvent.Reset -> {
                viewModelScope.launch {
                    stopwatchService.reset()
                }
            }
            HomeEvent.Settings -> {
                analyticsProvider.logEvent(StopwatchAction.Settings)
                navigate(HomeDirections.Settings)
            }
        }
    }
}
