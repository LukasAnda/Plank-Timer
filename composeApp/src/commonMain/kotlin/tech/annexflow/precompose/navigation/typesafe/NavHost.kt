package tech.annexflow.precompose.navigation.typesafe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition
import tech.annexflow.precompose.navigation.typesafe.internal.encodeToString

/**
 * Provides in place in the Compose hierarchy for self-contained navigation to occur.
 *
 * Once this is called, any Composable within the given [RouteBuilder] can be navigated to from
 * the provided [RouteBuilder].
 *
 * The builder passed into this method is [remember]ed. This means that for this NavHost, the
 * contents of the builder cannot be changed.
 *
 * @param navigator the Navigator for this host
 * @param initialRoute the route for the start destination
 * @param navTransition navigation transition for the scenes in this [TypesafeNavHost]
 * @param swipeProperties properties of swipe back navigation
 * @param builder the builder used to construct the graph
 */
@ExperimentalTypeSafeApi
@Composable
inline fun <reified T : Route> TypesafeNavHost(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    initialRoute: T,
    navTransition: NavTransition = remember { NavTransition() },
    swipeProperties: SwipeProperties? = null,
    noinline builder: RouteBuilder.() -> Unit,
) {
    TypesafeNavHost(
        modifier = modifier,
        navigator = navigator,
        initialRoute = initialRoute,
        serializer = serializer<T>(),
        navTransition = navTransition,
        swipeProperties = swipeProperties,
        builder = builder
    )
}

@ExperimentalTypeSafeApi
@Composable
fun <T : Route> TypesafeNavHost(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    initialRoute: T,
    serializer: KSerializer<T>,
    navTransition: NavTransition = remember { NavTransition() },
    swipeProperties: SwipeProperties? = null,
    builder: RouteBuilder.() -> Unit,
) {
    NavHost(
        modifier = modifier,
        navigator = navigator,
        initialRoute = initialRoute.encodeToString(serializer),
        navTransition = navTransition,
        swipeProperties = swipeProperties,
        builder = builder
    )
}
