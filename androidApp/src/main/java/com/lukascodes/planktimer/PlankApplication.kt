package com.lukascodes.planktimer

import android.app.Application
import com.lukascodes.planktimer.di.AppModule
import com.lukascodes.planktimer.services.analytics.AnalyticsProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class PlankApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        stopKoin()
        startKoin {
            androidContext(this@PlankApplication)
            logger(AndroidLogger())
            modules(
                AppModule.module,
                module {
                    single<AnalyticsProvider> { FirebaseAnalyticsProvider() }
                },
            )
        }
    }
}