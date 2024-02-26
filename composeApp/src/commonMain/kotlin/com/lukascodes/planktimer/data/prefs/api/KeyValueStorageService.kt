package com.lukascodes.planktimer.data.prefs.api

import com.lukascodes.planktimer.model.StopwatchState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.reflect.KMutableProperty1

interface KeyValueStorageService : Observable<KeyValueStorageService> {
    val coroutineDispatcher: CoroutineDispatcher

    var state: StopwatchState
    var realTimeMillisConfig: Long
    var observedTimeMillisConfig: Long

    suspend fun <T> get(property: KMutableProperty1<KeyValueStorageService, T>): T =
        withContext(coroutineDispatcher) {
            return@withContext property.get(this@KeyValueStorageService)
        }

    suspend fun <T> set(property: KMutableProperty1<KeyValueStorageService, T>, value: T) =
        withContext(coroutineDispatcher) {
            property.set(this@KeyValueStorageService, value)
            return@withContext get(property)
        }

    suspend fun <T> update(property: KMutableProperty1<KeyValueStorageService, T>, receiver: T.() -> T) =
        withContext(coroutineDispatcher) {
            return@withContext property.set(this@KeyValueStorageService, receiver(get(property)))
        }

    suspend fun <T> updateIf(
        property: KMutableProperty1<KeyValueStorageService, T>,
        condition: T.() -> Boolean,
        newValue: suspend () -> T,
    ) = withContext(coroutineDispatcher) {
        if (condition(get(property))) {
            property.set(this@KeyValueStorageService, newValue())
        }
        return@withContext get(property)
    }
}
