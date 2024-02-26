package com.lukascodes.planktimer.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
sealed interface StopwatchState {
    @Serializable
    data object Stopped: StopwatchState
    @Serializable
    data class Resumed(val startTime: Instant): StopwatchState
    @Serializable
    data class Paused(val startTime: Instant, val pauseTime: Instant): StopwatchState
}