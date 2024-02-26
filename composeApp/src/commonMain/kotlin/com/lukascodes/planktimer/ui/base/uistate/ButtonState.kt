package com.lukascodes.planktimer.ui.base.uistate

sealed class ButtonState(override val type: String) : Testable {
    data class Text(
        val text: StringDescription,
        override val testId: String,
        override val screenId: String,
        val enabled: Boolean = true,
        val startIcon: IconState? = null,
        val endIcon: IconState? = null,
    ) : ButtonState("textButton")

    data class Icon(
        val iconState: IconState,
        override val testId: String,
        override val screenId: String,
        val enabled: Boolean = true,
    ) : ButtonState("iconButton")
}