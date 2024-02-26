package tech.annexflow.precompose.navigation.typesafe.internal

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule
import moe.tlaster.precompose.navigation.QueryString
import tech.annexflow.precompose.navigation.typesafe.isOptional

@OptIn(ExperimentalSerializationApi::class)
internal class BackStackEntryDecoder(
    private val pathMap: Map<String, String>,
    private val queryString: QueryString?
) : AbstractDecoder() {
    override val serializersModule: SerializersModule by lazy { routeSerializersModule }
    private val json by lazyJson(serializersModule)

    private var isMain = true
    private var elementsCount = 0
    private var elementIndex = 0
    private var elementName = ""
    private var isElementOptional = false

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return if (isMain || (deserializer.descriptor.kind is PrimitiveKind)) {
            super.decodeSerializableValue(deserializer)
        } else {
            json.decodeFromString(deserializer, decodeString())
        }
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        elementName = descriptor.getElementName(elementIndex)
        isElementOptional = descriptor.isOptional(elementIndex)
        return elementIndex++
    }

    override fun decodeNotNullMark(): Boolean =
        pathMap[elementName] != null || queryString?.map?.get(elementName) != null

    override fun decodeInt(): Int = decodeValue().toInt()
    override fun decodeBoolean(): Boolean = decodeValue().toBooleanStrict()
    override fun decodeByte(): Byte = decodeValue().toByte()
    override fun decodeShort(): Short = decodeValue().toShort()
    override fun decodeLong(): Long = decodeValue().toLong()
    override fun decodeFloat(): Float = decodeValue().toFloat()
    override fun decodeDouble(): Double = decodeValue().toDouble()
    override fun decodeChar(): Char = decodeValue().first()
    override fun decodeString(): String = decodeValue()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeInt()

    override fun decodeValue(): String {
        val decodedValue =  if (isElementOptional) queryString!!.map[elementName]!!.first()
        else pathMap[elementName]
            ?: throw IllegalStateException("Element $elementName is not optional and not provided!")
        return UrlEncoder.decode(decodedValue)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        elementsCount = descriptor.elementsCount
        isMain = false
        return this
    }
}