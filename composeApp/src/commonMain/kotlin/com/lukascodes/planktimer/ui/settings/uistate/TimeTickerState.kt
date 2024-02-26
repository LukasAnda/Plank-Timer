package com.lukascodes.planktimer.ui.settings.uistate

import com.lukascodes.planktimer.ui.base.uistate.Testable

data class TimeTickerState(
    val hours: String,
    val hourValues: List<String>,
    val minutes: String,
    val minuteValues: List<String>,
    val seconds: String,
    val secondValues: List<String>,
    override val testId: String,
    override val screenId: String,
): Testable {
    override val type: String
        get() = "timeTicker"
}