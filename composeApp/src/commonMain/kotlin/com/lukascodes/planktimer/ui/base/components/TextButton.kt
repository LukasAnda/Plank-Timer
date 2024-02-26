package com.lukascodes.planktimer.ui.base.components

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.lukascodes.planktimer.ui.base.uistate.ButtonState

@Composable
fun TextButton(
    state: ButtonState.Text,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedButton(
        enabled = state.enabled,
        modifier = modifier.testTag(state.testTag),
        content = {
            Text(text = state.text.text())
        },
        onClick = onClick,
    )
}