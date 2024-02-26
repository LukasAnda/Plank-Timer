package com.lukascodes.planktimer.data.prefs.api

import kotlin.reflect.KProperty

interface PropertyObserver<T> {
    fun onValueChange(
        property: KProperty<T>,
        oldValue: T,
        newValue: T,
    )
}
