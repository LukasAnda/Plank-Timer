package tech.annexflow.precompose.navigation.typesafe

import tech.annexflow.precompose.navigation.typesafe.internal.encodeToStringFromSerializersModule

fun PopUpTo(route: Route, inclusive: Boolean = false) = moe.tlaster.precompose.navigation.PopUpTo(
    route = route.encodeToStringFromSerializersModule(),
    inclusive = inclusive,
)
