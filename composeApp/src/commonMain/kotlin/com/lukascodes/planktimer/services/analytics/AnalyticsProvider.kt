package com.lukascodes.planktimer.services.analytics

interface AnalyticsProvider {

    fun logEvent(eventName: String, eventParams: Map<String, Any> = emptyMap())
    fun logScreenView(screenName: String)
}

fun AnalyticsProvider.logEvent(analyticsEvent: AnalyticsEvent) {
    logEvent(analyticsEvent.eventName, analyticsEvent.data.filterNotNullValues())
}

@Suppress("UNCHECKED_CAST")
private fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> = filterValues { it != null } as Map<K, V>