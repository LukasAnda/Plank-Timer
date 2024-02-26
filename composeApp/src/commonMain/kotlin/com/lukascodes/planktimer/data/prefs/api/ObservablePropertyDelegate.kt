package com.lukascodes.planktimer.data.prefs.api

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

class ObservablePropertyDelegate<O : Observable<O>, T>(private val impl: ReadWriteProperty<O, T>) :
    ReadWriteProperty<O, T> {
    override fun getValue(thisRef: O, property: KProperty<*>): T {
        return impl.getValue(thisRef, property)
    }

    override fun setValue(
        thisRef: O,
        property: KProperty<*>,
        value: T,
    ) {
        val oldValue = getValue(thisRef, property)
        impl.setValue(thisRef, property, value)
        @Suppress("UNCHECKED_CAST")
        thisRef.notifyObservers(property as KProperty1<O, T>, oldValue, value)
    }
}
