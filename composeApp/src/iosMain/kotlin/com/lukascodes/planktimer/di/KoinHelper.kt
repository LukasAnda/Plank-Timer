package com.lukascodes.planktimer.di

import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

/**
 * A simple helper function that starts koin from iOS app
 * and gets iOS swift specific dependencies as params
 */
fun initKoin(
    analyticsProvider: AnalyticsProvider,
) {
    stopKoin()
    startKoin {
        modules(AppModule.module, module { single<AnalyticsProvider> { analyticsProvider } })
    }
}