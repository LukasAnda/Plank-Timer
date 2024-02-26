package tech.annexflow.precompose.navigation.typesafe.internal

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import tech.annexflow.precompose.navigation.typesafe.Route
import tech.annexflow.precompose.navigation.typesafe.generateRoutePattern
import kotlin.reflect.KClass

internal var routeSerializersModule: SerializersModule = EmptySerializersModule()
    private set

internal val routeSerializers : LinkedHashMap<String, KSerializer<out Route>> = linkedMapOf()

internal fun <T : Route> addRouteSerializer(
    kClass: KClass<T>,
    serializer: KSerializer<T>,
) {
    routeSerializers[generateRoutePattern(serializer)] = serializer
    routeSerializersModule += SerializersModule {
        polymorphic(Route::class) {
            subclass(kClass, serializer)
        }
    }
}