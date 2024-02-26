package com.lukascodes.planktimer.ui.base.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.lukascodes.planktimer.ui.base.uistate.IconState
import org.jetbrains.compose.resources.painterResource

@Composable
fun Icon(state: IconState, modifier: Modifier = Modifier, tint: Color = Color.Unspecified) {
    androidx.compose.material.Icon(
        modifier = modifier,
        painter = state.icon.painter(),
        contentDescription = state.contentDescription?.text(),
        tint = tint
    )
}