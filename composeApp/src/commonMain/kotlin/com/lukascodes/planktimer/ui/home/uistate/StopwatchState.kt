package com.lukascodes.planktimer.ui.home.uistate

import com.lukascodes.planktimer.ui.base.uistate.StringDescription
import com.lukascodes.planktimer.ui.base.uistate.Testable

data class StopwatchState(
    val realFormattedTime: StringDescription,
    val realTimeDescription: StringDescription,
    val observedFormattedTime: StringDescription,
    val observedTimeDescription: StringDescription,
    val realProgress: Float,
    val observedProgress: Float,
    override val testId: String,
    override val screenId: String,
): Testable {
    override val type: String
        get() = "stopwatch"
}