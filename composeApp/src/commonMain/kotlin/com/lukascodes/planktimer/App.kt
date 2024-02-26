package com.lukascodes.planktimer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lukascodes.planktimer.di.AppModule
import com.lukascodes.planktimer.theme.AppTheme
import com.lukascodes.planktimer.ui.nav.AppNavigation
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.compose.LocalKoinApplication
import org.koin.compose.LocalKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication
import org.koin.mp.KoinPlatformTools

@Composable
internal fun App() = AppTheme {
    KoinContext {
        PreComposeApp {
            Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
                AppNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(KoinInternalApi::class)
@Composable
internal fun KoinApp(
    application: KoinAppDeclaration,
    content: @Composable () -> Unit
) {
    val koinApplication = remember(application) {
        val alreadyExists = KoinPlatformTools.defaultContext().getOrNull() != null
        if (alreadyExists) {
            stopKoin()
        }
        startKoin(application)
    }
    CompositionLocalProvider(
        LocalKoinApplication provides koinApplication.koin,
        LocalKoinScope provides koinApplication.koin.scopeRegistry.rootScope
    ) {
        content()
    }
}

internal expect fun openUrl(url: String?)