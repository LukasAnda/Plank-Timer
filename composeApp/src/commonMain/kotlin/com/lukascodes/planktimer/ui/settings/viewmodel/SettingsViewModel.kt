package com.lukascodes.planktimer.ui.settings.viewmodel

import co.touchlab.kermit.Logger
import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.ui.base.uistate.ButtonState
import com.lukascodes.planktimer.ui.base.uistate.toDescription
import com.lukascodes.planktimer.ui.base.viewmodel.BaseViewModel
import com.lukascodes.planktimer.ui.base.viewmodel.updateData
import com.lukascodes.planktimer.ui.settings.navigation.SettingsDirections
import com.lukascodes.planktimer.ui.settings.uistate.TimeTickerState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SettingsViewModel(private val dataStore: KeyValueStorageService) : BaseViewModel<SettingsState, SettingsEvent, SettingsDirections>() {
    companion object {
        private const val SCREEN_ID = "settings"
    }

    private var observedDuration: Duration = Duration.INFINITE
    private var realDuration: Duration = Duration.INFINITE

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
                        observedHours.toInt().formatToTime(),
                        hourValues,
                        observedMinutes.formatToTime(),
                        minuteValues,
                        observedSeconds.formatToTime(),
                        secondValues,
                        "observedTime",
                        SCREEN_ID
                    ),
                    observedTimeDescription = "Select a time that you want to think it is".toDescription(),
                    realTimeTickerState = TimeTickerState(
                        realHours.toInt().formatToTime(),
                        hourValues,
                        realMinutes.formatToTime(),
                        minuteValues,
                        realSeconds.formatToTime(),
                        secondValues,
                        "realTime",
                        SCREEN_ID
                    ),
                    realTimeDescription = "Select a time that you want to actually pass in reality".toDescription(),
                    saveButton = ButtonState.Text(
                        text = "Save".toDescription(),
                        testId = "save",
                        screenId = SCREEN_ID,
                    ),
                    backButton = ButtonState.Text(
                        text = "Back".toDescription(),
                        testId = "back",
                        screenId = SCREEN_ID,
                    ),
                )
            }
        }
    }

    private fun Int.formatToTime() = if (this < 10) {
        "0$this"
    } else {
        "$this"
    }

    override fun onEvent(event: SettingsEvent) {
        super.onEvent(event)
        when (event) {
            SettingsEvent.Back -> {
                navigate(SettingsDirections.Back)
            }
            is SettingsEvent.ObservedTimeSettings -> {
                observedDuration = event.duration
            }
            is SettingsEvent.RealTimeSettings -> {
                realDuration = event.duration

            }
            SettingsEvent.Save -> {
                viewModelScope.launch {
                    Logger.e("Real duration: $realDuration , observedDuration: $observedDuration")
                    dataStore.set(KeyValueStorageService::observedTimeMillisConfig, observedDuration.inWholeMilliseconds)
                    dataStore.set(KeyValueStorageService::realTimeMillisConfig, realDuration.inWholeMilliseconds)
                    navigate(SettingsDirections.Back)
                }
            }
        }
    }
}
