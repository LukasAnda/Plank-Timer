package com.lukascodes.planktimer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lukascodes.planktimer.theme.AppTheme
import com.lukascodes.planktimer.ui.nav.AppNavigation
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext

@Composable
fun App() = AppTheme {
    KoinContext {
        PreComposeApp {
            Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
                AppNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

internal expect fun openUrl(url: String?)