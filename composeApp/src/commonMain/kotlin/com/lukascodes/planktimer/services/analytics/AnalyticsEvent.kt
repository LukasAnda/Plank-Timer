package com.lukascodes.planktimer.services.analytics

sealed class AnalyticsEvent(val eventName: String) {
    open val data = emptyMap<String, Any?>()

    sealed class StopwatchAction(
        private val actionName: String,
        private vararg val params: Pair<String, Any>,
    ) : AnalyticsEvent("stopwatch_action") {
        data object Start : StopwatchAction("start")
        data class Resume(private val secondsPassed: Int) : StopwatchAction("resume", "seconds_passed" to secondsPassed)
        data class Stop(private val secondsPassed: Int) : StopwatchAction("stop", "seconds_passed" to secondsPassed)
        data class Reset(private val secondsPassed: Int) : StopwatchAction("reset", "seconds_passed" to secondsPassed)
        data object Settings : StopwatchAction("settings")

        override val data: Map<String, Any?>
            get() = mapOf(
                "action" to actionName,
                *params
            )
    }

    sealed class SettingsAction(
        private val actionName: String,
        private val realSeconds: Int,
        private val observedSeconds: Int,
    ) : AnalyticsEvent("settings_action") {
        class Save(realSeconds: Int, observedSeconds: Int) : SettingsAction("save", realSeconds, observedSeconds)
        class Back(realSeconds: Int, observedSeconds: Int) : SettingsAction("back", realSeconds, observedSeconds)

        override val data: Map<String, Any?>
            get() = mapOf(
                "action" to actionName,
                "real_seconds" to realSeconds,
                "observed_seconds" to observedSeconds
            )
    }
}