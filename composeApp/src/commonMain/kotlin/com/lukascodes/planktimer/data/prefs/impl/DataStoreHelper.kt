package com.lukascodes.planktimer.data.prefs.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultValue: T): T =
    runBlocking {
        try {
            data.first()[key] ?: defaultValue
        } catch (e: Exception) {
            Logger.e(throwable = e) { "Can't get preference: $key" }
            defaultValue
        }
    }

fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, value: T?) =
    runBlocking<Unit> {
        edit {
            if (value == null) {
                it.remove(key)
            } else {
                it[key] = value
            }
        }
    }

class OptionalPreferenceDataStore<T>(
    private val dataStore: DataStore<Preferences>,
    private val serializer: KSerializer<T>,
    private val defaultValue: T? = null,
    private val propertyName: String? = null,
) : ReadWriteProperty<Any, T?> {


    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val savedString = dataStore.get(key = stringPreferencesKey(propertyName ?: property.name), defaultValue = "")
        return runCatching { Json.decodeFromString(serializer, savedString) }.getOrElse { defaultValue }
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: T?,
    ) {
        dataStore.set(
            key = stringPreferencesKey(
                propertyName
                    ?: property.name,
            ),
            value = value?.let { Json.encodeToString(serializer, value) },
        )
    }
}

class RequiredPreferenceDataStore<T>(
    private val dataStore: DataStore<Preferences>,
    private val serializer: KSerializer<T>,
    private val defaultValue: T,
    private val propertyName: String? = null,
) : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val savedString = dataStore.get(key = stringPreferencesKey(propertyName ?: property.name), defaultValue = "")
        return runCatching { Json.decodeFromString(serializer, savedString) }.getOrElse { defaultValue }
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: T,
    ) {
        dataStore.set(
            key = stringPreferencesKey(
                propertyName
                    ?: property.name,
            ),
            value = value?.let { Json.encodeToString(serializer, value) },
        )
    }
}
