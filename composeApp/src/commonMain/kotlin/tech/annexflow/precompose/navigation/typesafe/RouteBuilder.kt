package tech.annexflow.precompose.navigation.typesafe

import androidx.compose.runtime.Composable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition
import tech.annexflow.precompose.navigation.typesafe.internal.decodePath
import kotlin.reflect.KClass

/**
 * Add the scene [Composable] to the [RouteBuilder]
 * @param navTransition navigation transition for current scene
 * @param swipeProperties swipe back navigation properties for current scene
 * @param content composable for the destination
 */
inline fun <reified T : Route> RouteBuilder.scene(
    deepLinks: List<String> = emptyList(),
    navTransition: NavTransition? = null,
    swipeProperties: SwipeProperties? = null,
    noinline content: @Composable T.(BackStackEntry) -> Unit,
) = scene(
    kClass = T::class,
    serializer = serializer(),
    deepLinks = deepLinks,
    navTransition = navTransition,
    swipeProperties = swipeProperties,
    content = content
)

fun <T : Route> RouteBuilder.scene(
    kClass: KClass<T>,
    serializer: KSerializer<T>,
    deepLinks: List<String> = emptyList(),
    navTransition: NavTransition? = null,
    swipeProperties: SwipeProperties? = null,
    content: @Composable T.(BackStackEntry) -> Unit,
) {
    registerRouteType(kClass = kClass, serializer = serializer)
    scene(
        route = generateRoutePattern(serializer),
        deepLinks = deepLinks,
        navTransition = navTransition,
        swipeProperties = swipeProperties,
    ) { backStackEntry ->
        backStackEntry.decodePath(serializer).content(backStackEntry)
    }
}

/**
 * Add a group of [Composable] to the [RouteBuilder]
 * @param initialRoute initial route for the group
 * @param content composable for the destination
 */
inline fun <reified T : Route> RouteBuilder.group(
    initialRoute: String,
    noinline content: RouteBuilder.() -> Unit,
) {
    group(
        kClass = T::class,
        serializer = serializer<T>(),
        initialRoute = initialRoute,
        content = content
    )
}

fun <T : Route> RouteBuilder.group(
    kClass: KClass<T>,
    serializer: KSerializer<T>,
    initialRoute: String,
    content: RouteBuilder.() -> Unit,
) {
    registerRouteType(kClass = kClass, serializer = serializer)
    group(
        route = generateRoutePattern(serializer),
        initialRoute = initialRoute,
        content = content
    )
}

/**
 * Add the dialog [Composable] to the [RouteBuilder], which will show over the scene
 * @param content composable for the destination
 */
inline fun <reified T : Route> RouteBuilder.dialog(
    noinline content: @Composable T.(BackStackEntry) -> Unit,
) = dialog(
    kClass = T::class,
    serializer = serializer(),
    content = content
)

fun <T : Route> RouteBuilder.dialog(
    kClass: KClass<T>,
    serializer: KSerializer<T>,
    content: @Composable T.(BackStackEntry) -> Unit,
) {
    registerRouteType(kClass = kClass, serializer = serializer)
    dialog(
        route = generateRoutePattern(serializer)
    ) { backStackEntry ->
        backStackEntry.decodePath(serializer).content(backStackEntry)
    }
}

/**
 * Add the floating [Composable] to the [RouteBuilder], which will show over the scene
 * @param content composable for the destination
 */
inline fun <reified T : Route> RouteBuilder.floating(
    noinline content: @Composable T.(BackStackEntry) -> Unit,
) = floating(
    kClass = T::class,
    serializer = serializer(),
    content = content
)

fun <T : Route> RouteBuilder.floating(
    kClass: KClass<T>,
    serializer: KSerializer<T>,
    content: @Composable T.(BackStackEntry) -> Unit,
) {
    registerRouteType(kClass = kClass, serializer = serializer)
    floating(
        route = generateRoutePattern(serializer)
    ) { backStackEntry ->
        backStackEntry.decodePath(serializer).content(backStackEntry)
    }
}