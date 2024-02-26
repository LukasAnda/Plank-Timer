package com.lukascodes.planktimer.di

import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.data.prefs.impl.DataStoreStorageService
import com.lukascodes.planktimer.ui.home.viewmodel.HomeViewModel
import com.lukascodes.planktimer.ui.settings.viewmodel.SettingsViewModel
import org.koin.dsl.module

object AppModule {
    val module = module {
        factory { HomeViewModel(get()) }
        factory { SettingsViewModel(get()) }
        single<KeyValueStorageService> { DataStoreStorageService() }
    }
}