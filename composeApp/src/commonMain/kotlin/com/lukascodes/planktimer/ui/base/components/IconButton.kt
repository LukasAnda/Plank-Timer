package com.lukascodes.planktimer.ui.base.components

import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.lukascodes.planktimer.ui.base.uistate.ButtonState

@Composable
fun IconButton(state: ButtonState.Icon, modifier: Modifier = Modifier, onClick: () -> Unit) {
    LargeFloatingActionButton(
        modifier = modifier.testTag(state.testTag),
        content = {
            Icon(
                state = state.iconState,
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        },
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
    )
}