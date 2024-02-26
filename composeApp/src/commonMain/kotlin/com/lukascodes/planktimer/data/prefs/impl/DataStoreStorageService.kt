package com.lukascodes.planktimer.data.prefs.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lukascodes.planktimer.data.prefs.api.KeyValueStorageService
import com.lukascodes.planktimer.data.prefs.api.Observable
import com.lukascodes.planktimer.data.prefs.api.ObservablePropertyDelegate
import com.lukascodes.planktimer.data.prefs.api.dataStorePreferences
import com.lukascodes.planktimer.model.StopwatchState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.serializer
import kotlin.properties.ReadWriteProperty
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DataStoreStorageService : KeyValueStorageService,
    Observable<KeyValueStorageService> by ObservableImpl() {

    private val dataStore: DataStore<Preferences> = dataStorePreferences(
        coroutineScope = CoroutineScope(Dispatchers.IO),
        corruptionHandler = null,
        migrations = emptyList(),
    )

    override val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    override var state: StopwatchState by requiredPropertyDelegate("stopwatchState") { StopwatchState.Stopped }
    override var realTimeMillisConfig: Long by requiredPropertyDelegate("realTime") { 1.minutes.inWholeMilliseconds }
    override var observedTimeMillisConfig: Long by requiredPropertyDelegate("observedTime") { 30.seconds.inWholeMilliseconds }


    private inline fun <reified T> optionalPropertyDelegate(
        propertyName: String? = null,
        noinline defaultValue: () -> T? = { null },
    ): ReadWriteProperty<DataStoreStorageService, T?> {
        return ObservablePropertyDelegate(
            OptionalPreferenceDataStore(
                dataStore = dataStore,
                serializer = serializer(),
                defaultValue = defaultValue(),
                propertyName = propertyName,
            ),
        )
    }

    private inline fun <reified T> requiredPropertyDelegate(
        propertyName: String? = null,
        noinline defaultValue: () -> T,
    ): ReadWriteProperty<DataStoreStorageService, T> {
        return ObservablePropertyDelegate(
            RequiredPreferenceDataStore(
                dataStore = dataStore,
                serializer = serializer(),
                defaultValue = defaultValue(),
                propertyName = propertyName,
            ),
        )
    }

}
