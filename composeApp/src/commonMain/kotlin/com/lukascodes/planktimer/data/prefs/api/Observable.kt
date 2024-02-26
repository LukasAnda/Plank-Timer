package com.lukascodes.planktimer.data.prefs.api

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

interface Observable<O : Observable<O>> {
    fun <T> notifyObservers(
        property: KProperty1<O, T>,
        oldValue: T,
        newValue: T,
    )

    fun <T> addObserver(property: KProperty1<O, T>, observer: PropertyObserver<T>)

    fun <T> removeObserver(property: KProperty1<O, T>, observer: PropertyObserver<T>)
}

fun <O : Observable<O>, T> O.observeAsFlow(property: KProperty1<O, T>) =
    callbackFlow {
        val observer = addObserver(property, true) { _, newValue ->
            this.trySend(newValue)
        }
        awaitClose {
            removeObserver(property, observer)
        }
    }

fun <O : Observable<O>, T> O.addObserver(
    property: KProperty1<O, T>,
    receiveCurrentValue: Boolean = false,
    changeHandler: (oldValue: T, newValue: T) -> Unit,
): PropertyObserver<T> {
    val propertyObserver: PropertyObserver<T> = object : PropertyObserver<T> {
        override fun onValueChange(
            property: KProperty<T>,
            oldValue: T,
            newValue: T,
        ) {
            changeHandler(oldValue, newValue)
        }
    }
    this.addObserver(property = property, observer = propertyObserver)
    if (receiveCurrentValue) {
        val currentValue = property.get(this)
        changeHandler(currentValue, currentValue)
    }
    return propertyObserver
}
