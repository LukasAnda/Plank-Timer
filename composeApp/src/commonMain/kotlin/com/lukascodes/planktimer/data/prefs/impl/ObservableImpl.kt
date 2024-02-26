package com.lukascodes.planktimer.data.prefs.impl

import com.lukascodes.planktimer.data.prefs.api.Observable
import com.lukascodes.planktimer.data.prefs.api.PropertyObserver
import kotlin.reflect.KProperty1

class ObservableImpl<O : Observable<O>> : Observable<O> {
    private var observers = mutableMapOf<String, MutableList<PropertyObserver<*>>>()
    private val propertyVersions = mutableMapOf<String, Int>()

    override fun <T> addObserver(property: KProperty1<O, T>, observer: PropertyObserver<T>) {
        val observersForProperty = observers.getOrPut(property.name) {
            mutableListOf()
        }
        observersForProperty.add(observer)
    }

    override fun <T> removeObserver(property: KProperty1<O, T>, observer: PropertyObserver<T>) {
        val observersForProperty = observers.getOrPut(property.name) {
            mutableListOf()
        }
        observersForProperty.removeAll { it == observer }
    }

    override fun <T> notifyObservers(
        property: KProperty1<O, T>,
        oldValue: T,
        newValue: T,
    ) {
        val currentVersion = (propertyVersions[property.name] ?: 0) + 1
        propertyVersions[property.name] = currentVersion
        val observers = observers[property.name]?.toList() ?: emptyList()
        observers.forEach {
            @Suppress("UNCHECKED_CAST")
            (it as PropertyObserver<T>).onValueChange(property, oldValue, newValue)
            if (currentVersion != propertyVersions[property.name]) return
        }
    }
}
