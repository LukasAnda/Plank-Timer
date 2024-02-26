package tech.annexflow.precompose.navigation.typesafe

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.StructureKind
import tech.annexflow.precompose.navigation.typesafe.internal.addRouteSerializer
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
private val allowedClassKinds =
    listOf(StructureKind.CLASS, StructureKind.OBJECT, PolymorphicKind.OPEN)

/**
 * Registers a route type along with its serializer, ensuring it conforms to allowed serialization kinds.
 *
 * @param kClass The KClass object representing the class type of the route.
 * @param serializer The KSerializer object responsible for serializing and deserializing the route.
 *
 * @throws IllegalArgumentException If the provided serializer's descriptor kind is not in the allowedClassKinds.
 */
@OptIn(ExperimentalSerializationApi::class)
fun <T : Route> registerRouteType(
    kClass: KClass<T>,
    serializer: KSerializer<T>,
) {
    require(value = serializer.descriptor.kind in allowedClassKinds) {
        "${serializer.descriptor.kind} kind is not allowed! Use data class or data object."
    }
    addRouteSerializer(kClass, serializer)
}