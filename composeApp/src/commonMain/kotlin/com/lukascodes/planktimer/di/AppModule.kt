package com.lukascodes.planktimer.di

import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.data.prefs.impl.DataStoreStorageService
import com.lukascodes.planktimer.services.stopwatch.DefaultStopwatchService
import com.lukascodes.planktimer.services.stopwatch.StopwatchService
import com.lukascodes.planktimer.ui.home.viewmodel.HomeViewModel
import com.lukascodes.planktimer.ui.settings.viewmodel.SettingsViewModel
import org.koin.dsl.module

object AppModule {
    val module = module {
        factory { HomeViewModel(get(), get(), get()) }
        factory { SettingsViewModel(get(), get()) }
        single<KeyValueStorageService> { DataStoreStorageService() }
        single<StopwatchService> { DefaultStopwatchService(get(), get()) }
    }
}