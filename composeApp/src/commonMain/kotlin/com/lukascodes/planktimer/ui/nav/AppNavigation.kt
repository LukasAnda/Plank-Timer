package com.lukascodes.planktimer.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lukascodes.planktimer.ui.home.navigation.HomeDestination
import com.lukascodes.planktimer.ui.settings.navigation.SettingsDestination
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.rememberNavigator
import tech.annexflow.precompose.navigation.typesafe.ExperimentalTypeSafeApi
import tech.annexflow.precompose.navigation.typesafe.TypesafeNavHost
import tech.annexflow.precompose.navigation.typesafe.scene

@OptIn(ExperimentalTypeSafeApi::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navigator = rememberNavigator()
    val transitionOffset = 3000
    val transitionDuration = 3000
    BackHandler {
        navigator.goBack()
    }
    TypesafeNavHost(
        modifier = modifier,
        navigator = navigator,
        initialRoute = AppRoutes.Home,
        swipeProperties = SwipeProperties(),
        // TODO Animations not working for now in current PreCompose version
//        navTransition = remember {
//            NavTransition(
//                createTransition = slideInHorizontally(
//                    initialOffsetX = { transitionOffset },
//                    animationSpec = tween(transitionDuration),
//                ) + fadeIn(animationSpec = tween(transitionDuration)),
//                destroyTransition = slideOutHorizontally(
//                    targetOffsetX = { transitionOffset },
//                    animationSpec = tween(transitionDuration),
//                ) + fadeOut(animationSpec = tween(transitionDuration)),
//                resumeTransition = slideInHorizontally(
//                    initialOffsetX = { -transitionOffset },
//                    animationSpec = tween(transitionDuration),
//                ) + fadeIn(animationSpec = tween(transitionDuration)),
//                pauseTransition = slideOutHorizontally(
//                    targetOffsetX = { -transitionOffset },
//                    animationSpec = tween(transitionDuration),
//                ) + fadeOut(animationSpec = tween(transitionDuration)),
//            )
//        },
    ) {
        scene<AppRoutes.Home> {
            HomeDestination(navigator, Modifier.fillMaxSize())
        }
        scene<AppRoutes.Settings> {
            SettingsDestination(navigator, Modifier.fillMaxSize())
        }
    }
}