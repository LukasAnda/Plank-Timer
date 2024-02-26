package com.lukascodes.planktimer.ui.base.uistate

import org.jetbrains.compose.resources.StringResource

data class TextState(
    val text: StringDescription,
    override val testId: String,
    override val screenId: String,
) : Testable {
    override val type: String
        get() = "text"
}