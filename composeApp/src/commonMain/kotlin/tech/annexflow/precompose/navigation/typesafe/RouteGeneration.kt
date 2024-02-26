package tech.annexflow.precompose.navigation.typesafe

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.serializer
import tech.annexflow.precompose.navigation.typesafe.internal.createRouteBase
import tech.annexflow.precompose.navigation.typesafe.internal.encodeToString

inline fun <reified T : Route> generateRoutePattern(): String =
    generateRoutePattern(serializer<T>())

@OptIn(ExperimentalSerializationApi::class)
fun <T : Route> generateRoutePattern(serializer: KSerializer<T>): String {
    val route = createRouteBase(serializationStrategy = serializer)
    val descriptor = serializer.descriptor
    if (descriptor.noNeedToParseElements) {
        return route
    }
    val path = StringBuilder()
    (0 until descriptor.elementsCount).forEach { index ->
        val name = descriptor.getElementName(index = index)
        if (!descriptor.isOptional(index = index)) {
            path.append("/{$name}")
        }
    }
    return route + path.toString()
}

@OptIn(ExperimentalSerializationApi::class)
internal val SerialDescriptor.noNeedToParseElements: Boolean get() =
    elementsCount == 0 || kind == PolymorphicKind.SEALED

@OptIn(ExperimentalSerializationApi::class)
internal fun SerialDescriptor.isOptional(index: Int): Boolean =
    isElementOptional(index) || getElementDescriptor(index).isNullable

inline fun <reified T : Route> T.generateRoutePattern(): String =
    this.generateRoutePattern(serializer = serializer<T>())

fun <T : Route> T.generateRoutePattern(
    serializer: KSerializer<T>
): String = this.encodeToString(serializer)