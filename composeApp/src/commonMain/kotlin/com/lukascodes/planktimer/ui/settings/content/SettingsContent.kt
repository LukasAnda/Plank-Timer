package com.lukascodes.planktimer.ui.settings.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukascodes.planktimer.ui.base.components.TextButton
import com.lukascodes.planktimer.ui.settings.components.TimeTicker
import com.lukascodes.planktimer.ui.settings.viewmodel.SettingsEvent
import com.lukascodes.planktimer.ui.settings.viewmodel.SettingsState

@Composable
fun SettingsContent(
    uiState: SettingsState,
    modifier: Modifier = Modifier,
    onEvent: (SettingsEvent) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiState.observedTimeDescription.text(),
                style = MaterialTheme.typography.headlineSmall,
            )
            TimeTicker(
                modifier = Modifier.padding(top = 16.dp),
                state = uiState.observedTimeTickerState,
            ) {
                onEvent(SettingsEvent.ObservedTimeSettings(it))
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiState.realTimeDescription.text(),
                style = MaterialTheme.typography.headlineSmall,
            )
            TimeTicker(
                modifier = Modifier.padding(top = 16.dp),
                state = uiState.realTimeTickerState,
            ) {
                onEvent(SettingsEvent.RealTimeSettings(it))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                state = uiState.backButton,
                onClick = {
                    onEvent(SettingsEvent.Back)
                }
            )
            TextButton(
                state = uiState.saveButton,
                onClick = {
                    onEvent(SettingsEvent.Save)
                }
            )
        }
    }
}