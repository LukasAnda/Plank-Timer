package com.lukascodes.planktimer

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider

class FirebaseAnalyticsProvider : AnalyticsProvider {
    override fun logEvent(eventName: String, eventParams: Map<String, Any>) {
        Firebase.analytics.logEvent(eventName, bundleOf(*eventParams.toList().toTypedArray()))
    }

    override fun logScreenView(screenName: String) {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "Main")
        }
    }

}