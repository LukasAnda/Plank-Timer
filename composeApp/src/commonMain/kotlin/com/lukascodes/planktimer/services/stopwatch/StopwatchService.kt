package com.lukascodes.planktimer.services.stopwatch

import kotlin.time.Duration

interface StopwatchService {
    suspend fun togglePlayPause()
    suspend fun reset()
    suspend fun getStopwatchTimes(): StopwatchTimes
}

data class StopwatchTimes(
    val realTime: Duration,
    val observedTime: Duration,
)