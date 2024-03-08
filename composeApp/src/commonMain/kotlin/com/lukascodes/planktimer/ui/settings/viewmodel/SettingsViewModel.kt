package com.lukascodes.planktimer.ui.settings.viewmodel

import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import com.lukascodes.planktimer.services.analytics.logEvent
import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.base.uistate.toDescription
import com.lukascodes.planktimer.ui.base.viewmodel.BaseViewModel
import com.lukascodes.planktimer.ui.base.viewmodel.updateData
import com.lukascodes.planktimer.ui.settings.navigation.SettingsDirections
import com.lukascodes.planktimer.ui.settings.uistate.TimeTickerState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope
import net.sergeych.sprintf.sprintf
import plank_timer.composeapp.generated.resources.Res
import plank_timer.composeapp.generated.resources.back
import plank_timer.composeapp.generated.resources.observed_time_settings_hint
import plank_timer.composeapp.generated.resources.real_time_settings_hint
import plank_timer.composeapp.generated.resources.save
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SettingsViewModel(private val dataStore: KeyValueStorageService, analyticsProvider: AnalyticsProvider) : BaseViewModel<SettingsState, SettingsEvent, SettingsDirections>(analyticsProvider) {
    companion object {
        private const val SCREEN_ID = "settings"
    }

    private var observedDuration: Duration = Duration.INFINITE
    private var realDuration: Duration = Duration.INFINITE
    override val screenView: String
        get() = "Settings"

    override fun ProducerScope<Unit>.onLifecycleStarted() {
        launch {
            observedDuration = dataStore
                .get(KeyValueStorageService::observedTimeMillisConfig)
                .milliseconds

            val (observedHours, observedMinutes, observedSeconds) = observedDuration
                .toComponents { hours, minutes, seconds, _ ->
                    Triple(hours, minutes, seconds)
                }

            realDuration = dataStore
                .get(KeyValueStorageService::realTimeMillisConfig)
                .milliseconds
            val (realHours, realMinutes, realSeconds) = realDuration
                .toComponents { hours, minutes, seconds, _ ->
                    Triple(hours, minutes, seconds)
                }

            val hourValues = (0..99)
                .map { it.formatToTime() }
                .asReversed()
            val minuteValues = (0..59).map { it.formatToTime() }
                .asReversed()
            val secondValues = (0..59)
                .map { it.formatToTime() }
                .asReversed()

            uiState.updateData {
                SettingsState(
                    observedTimeTickerState = TimeTickerState(
                        observedHours.formatToTime(),
                        hourValues,
                        observedMinutes.formatToTime(),
                        minuteValues,
                        observedSeconds.formatToTime(),
                        secondValues,
                        "observedTime",
                        SCREEN_ID
                    ),
                    observedTimeDescription = Res.string.observed_time_settings_hint.toDescription(),
                    realTimeTickerState = TimeTickerState(
                        realHours.formatToTime(),
                        hourValues,
                        realMinutes.formatToTime(),
                        minuteValues,
                        realSeconds.formatToTime(),
                        secondValues,
                        "realTime",
                        SCREEN_ID
                    ),
                    realTimeDescription = Res.string.real_time_settings_hint.toDescription(),
                    saveButton = ButtonState.Text(
                        text = Res.string.save.toDescription(),
                        testId = "save",
                        screenId = SCREEN_ID,
                    ),
                    backButton = ButtonState.Text(
                        text = Res.string.back.toDescription(),
                        testId = "back",
                        screenId = SCREEN_ID,
                    ),
                )
            }
        }
    }

    private fun Number.formatToTime() = "%02d".sprintf(this)

    override fun onEvent(event: SettingsEvent) {
        super.onEvent(event)
        when (event) {
            SettingsEvent.Back -> {
                viewModelScope.launch {
                    analyticsProvider.logEvent(
                        AnalyticsEvent.SettingsAction.Back(
                            realSeconds = realDuration.inWholeSeconds.toInt(),
                            observedSeconds = observedDuration.inWholeSeconds.toInt(),
                        )
                    )
                    navigate(SettingsDirections.Back)
                }
            }
            is SettingsEvent.ObservedTimeSettings -> {
                observedDuration = event.duration
            }
            is SettingsEvent.RealTimeSettings -> {
                realDuration = event.duration

            }
            SettingsEvent.Save -> {
                viewModelScope.launch {
                    analyticsProvider.logEvent(
                        AnalyticsEvent.SettingsAction.Save(
                            realSeconds = realDuration.inWholeSeconds.toInt(),
                            observedSeconds = observedDuration.inWholeSeconds.toInt(),
                        )
                    )

                    dataStore.set(KeyValueStorageService::observedTimeMillisConfig, observedDuration.inWholeMilliseconds)
                    dataStore.set(KeyValueStorageService::realTimeMillisConfig, realDuration.inWholeMilliseconds)
                    navigate(SettingsDirections.Back)
                }
            }
        }
    }
}
