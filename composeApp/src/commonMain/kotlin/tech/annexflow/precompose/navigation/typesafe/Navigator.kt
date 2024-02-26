package tech.annexflow.precompose.navigation.typesafe

import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import tech.annexflow.precompose.navigation.typesafe.internal.encodeToStringFromSerializersModule

/**
 * Navigate to a route in the current RouteGraph.
 *
 * @param route route for the destination
 * @param options navigation options for the destination
 */
fun Navigator.navigate(
    route: Route,
    options: NavOptions? = null
) = navigate(
    route = route.encodeToStringFromSerializersModule(),
    options = options
)

/**
 * Navigate to a route in the current RouteGraph and wait for result.
 * @param route route for the destination
 * @param options navigation options for the destination
 * @return result from the destination
 */
suspend fun Navigator.navigateForResult(
    route: Route,
    options: NavOptions? = null
) = navigateForResult(route.encodeToStringFromSerializersModule(), options)