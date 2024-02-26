package tech.annexflow.precompose.navigation.typesafe

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import moe.tlaster.precompose.navigation.BackStackEntry
import tech.annexflow.precompose.navigation.typesafe.internal.decodePath
import tech.annexflow.precompose.navigation.typesafe.internal.routeSerializers

@OptIn(ExperimentalSerializationApi::class)
@ExperimentalTypeSafeApi
val BackStackEntry.typesafeRoute : Route? get() {
    val deserializer = routeSerializers[route.route] ?: return null
    if(deserializer.descriptor.kind == PolymorphicKind.SEALED) return null
    return decodePath(deserializer = deserializer)
}