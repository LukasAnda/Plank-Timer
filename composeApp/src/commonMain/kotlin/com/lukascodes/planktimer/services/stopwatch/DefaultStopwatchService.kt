package com.lukascodes.planktimer.services.stopwatch

import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.model.StopwatchState
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent.StopwatchAction.Resume
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent.StopwatchAction.Start
import com.lukascodes.planktimer.services.analytics.AnalyticsEvent.StopwatchAction.Stop
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import com.lukascodes.planktimer.services.analytics.logEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DefaultStopwatchService(
    private val analyticsProvider: AnalyticsProvider,
    private val storageService: KeyValueStorageService,
) : StopwatchService {
    override suspend fun togglePlayPause() {
        storageService.update(KeyValueStorageService::state) {
            when (this) {
                is StopwatchState.Paused -> {
                    val newStartTime = this.startTime + (System.now() - this.pauseTime)

                    analyticsProvider.logEvent(
                        analyticsEvent = Resume(
                            secondsPassed = (System.now() - newStartTime).inWholeSeconds.toInt()
                        )
                    )

                    StopwatchState.Resumed(newStartTime)
                }
                is StopwatchState.Resumed -> {
                    analyticsProvider.logEvent(
                        analyticsEvent = Stop(
                            secondsPassed = (System.now() - this.startTime).inWholeSeconds.toInt()
                        )
                    )

                    StopwatchState.Paused(this.startTime, System.now())
                }
                StopwatchState.Stopped -> {
                    analyticsProvider.logEvent(analyticsEvent = Start)

                    StopwatchState.Resumed(System.now())
                }
            }
        }
    }

    override suspend fun reset() {
        val passedSeconds = when (val currentState = storageService.get(KeyValueStorageService::state)) {
            is StopwatchState.Paused -> System.now() - currentState.startTime
            is StopwatchState.Resumed -> System.now() - currentState.startTime
            StopwatchState.Stopped -> 0.seconds
        }

        analyticsProvider.logEvent(
            AnalyticsEvent.StopwatchAction.Reset(passedSeconds.inWholeSeconds.toInt())
        )

        storageService.set(KeyValueStorageService::state, StopwatchState.Stopped)
    }

    override suspend fun getStopwatchTimes(): StopwatchTimes {
        val observedMillisConfig = storageService.get(KeyValueStorageService::observedTimeMillisConfig)
        val realMillisConfig = storageService.get(KeyValueStorageService::realTimeMillisConfig)
        val ratio = observedMillisConfig.toDouble() / realMillisConfig

        val state = storageService.get(KeyValueStorageService::state)

        val realTime: Duration
        val observedTime: Duration

        when (state) {
            is StopwatchState.Paused -> {
                realTime = state.pauseTime - state.startTime
                observedTime = realTime * ratio
            }
            is StopwatchState.Resumed -> {
                realTime = System.now() - state.startTime
                observedTime = realTime * ratio
            }
            StopwatchState.Stopped -> {
                realTime = 0.seconds
                observedTime = 0.seconds
            }
        }

        return StopwatchTimes(realTime = realTime, observedTime = observedTime)
    }

}