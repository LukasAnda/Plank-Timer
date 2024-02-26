package com.lukascodes.planktimer.ui.base.uistate

interface Testable {
    val testId: String
    val screenId: String
    val type: String

    val testTag: String
        get() = "$screenId.$type.$testId"
}

