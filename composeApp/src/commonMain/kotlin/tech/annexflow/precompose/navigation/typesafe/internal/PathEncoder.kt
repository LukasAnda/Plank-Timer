package tech.annexflow.precompose.navigation.typesafe.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import tech.annexflow.precompose.navigation.typesafe.isOptional

@OptIn(ExperimentalSerializationApi::class)
internal class PathEncoder(
    private val url: StringBuilder,
) : AbstractEncoder() {
    override val serializersModule: SerializersModule by lazy { routeSerializersModule }
    private val json by lazyJson(serializersModule)
    private var isMain = true
    private var elementName = ""
    private var elementOptional = false
    private var queryParams = StringBuilder()

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        if (isMain || serializer.descriptor.kind is PrimitiveKind) {
            super.encodeSerializableValue(serializer, value)
        } else {
            encodeString(json.encodeToString(serializer, value))
        }
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        elementName = descriptor.getElementName(index)
        elementOptional = descriptor.isOptional(index)
        return true
    }

    override fun encodeNull() {}
    override fun encodeInt(value: Int) = appendValue(value.toString())
    override fun encodeLong(value: Long) = appendValue(value.toString())
    override fun encodeFloat(value: Float) = appendValue(value.toString())
    override fun encodeBoolean(value: Boolean) = appendValue(value.toString())
    override fun encodeString(value: String) = appendValue(value)
    override fun encodeDouble(value: Double) = appendValue(value.toString())
    override fun encodeByte(value: Byte) = appendValue(value.toString())
    override fun encodeShort(value: Short) = appendValue(value.toString())
    override fun encodeChar(value: Char) = appendValue(value.toString())
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = encodeInt(index)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        isMain = false
        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (queryParams.isNotBlank()) {
            queryParams[0] = '?'
        }
        url.append(queryParams)
    }

    private fun appendValue(value: String) {
        val encodedString = UrlEncoder.encode(value)
        if (elementOptional) {
            queryParams.append("&$elementName=$encodedString")
        } else {
            url.append("/$encodedString")
        }
    }
}