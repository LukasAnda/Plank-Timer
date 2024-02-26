package tech.annexflow.precompose.navigation.typesafe.internal

import kotlin.Char.Companion.MIN_HIGH_SURROGATE
import kotlin.Char.Companion.MIN_LOW_SURROGATE

internal object UrlEncoder {
    fun decode(source: String): String {
        if (source.isEmpty()) {
            return source
        }

        val length = source.length
        val result = StringBuilder(length)
        var bytesBuffer: ByteArray? = null
        var bytesPos = 0
        var index = 0
        var started = false
        while (index < length) {
            val character = source[index]
            if (character == '%') {
                if (!started) {
                    result.append(source, 0, index)
                    started = true
                }
                if (bytesBuffer == null) {
                    bytesBuffer = ByteArray(size = (length - index) / 3)
                }
                index++
                require(length >= index + 2) { "Incomplete trailing escape ($character) pattern" }
                try {
                    val v = source.substring(index, index + 2).toInt(16)
                    require(v in 0..0xFF) { "Illegal escape value" }
                    bytesBuffer[bytesPos++] = v.toByte()
                    index += 2
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException(
                        "Illegal characters in escape sequence: $e.message",
                        e
                    )
                }
            } else {
                if (bytesBuffer != null) {
                    result.append(bytesBuffer.decodeToString(startIndex = 0, endIndex = bytesPos))
                    started = true
                    bytesBuffer = null
                    bytesPos = 0
                }
                if (started) {
                    result.append(character)
                }
                index++
            }
        }

        if (bytesBuffer != null) {
            result.append(bytesBuffer.decodeToString(startIndex = 0, endIndex = bytesPos))
        }

        return if (!started) source else result.toString()
    }

    fun encode(source: String): String {
        if (source.isEmpty()) {
            return source
        }
        var result: StringBuilder? = null
        var index = 0
        while (index < source.length) {
            val character = source[index]
            if (character.isUnreserved) {
                result?.append(character)
                index++
            } else {
                if (result == null) {
                    result = StringBuilder(source.length)
                    result.append(source, 0, index)
                }
                val codePoint = source.codePointAt(index)
                when {
                    codePoint < 0x80 -> {
                        result.appendEncodedByte(codePoint)
                        index++
                    }

                    isBasicMultilingualPlaneCodePoint(codePoint) -> {
                        for (byte in character.toString().encodeToByteArray()) {
                            result.appendEncodedByte(byte.toInt())
                        }
                        index++
                    }

                    isSupplementaryCodePoint(codePoint) -> {
                        val high = highSurrogateOf(codePoint)
                        val low = lowSurrogateOf(codePoint)
                        for (byte in charArrayOf(high, low).concatToString().encodeToByteArray()) {
                            result.appendEncodedByte(byte.toInt())
                        }
                        index += 2
                    }
                }
            }
        }

        return result?.toString() ?: source
    }
}

private val hexDigits = "0123456789ABCDEF".toCharArray()

private val unreservedChars = BooleanArray('z'.code + 1).apply {
    set('-'.code, true)
    set('.'.code, true)
    set('_'.code, true)
    for (c in '0'..'9') {
        set(c.code, true)
    }
    for (c in 'A'..'Z') {
        set(c.code, true)
    }
    for (c in 'a'..'z') {
        set(c.code, true)
    }
}

private val Char.isUnreserved: Boolean get() = this <= 'z' && unreservedChars[code]

private fun StringBuilder.appendEncodedDigit(digit: Int) {
    this.append(hexDigits[digit and 0x0F])
}

private fun StringBuilder.appendEncodedByte(character: Int) {
    this.append("%")
    this.appendEncodedDigit(digit = character shr 4)
    this.appendEncodedDigit(digit = character)
}

private fun CharSequence.codePointAt(index: Int): Int {
    if (index !in indices) throw IndexOutOfBoundsException("index $index was not in range $indices")

    val firstChar = this[index]
    if (firstChar.isHighSurrogate()) {
        val nextChar = getOrNull(index = index + 1)
        if (nextChar?.isLowSurrogate() == true) {
            return toCodePoint(highSurrogate = firstChar, lowSurrogate = nextChar)
        }
    }

    return firstChar.code
}

private fun isSupplementaryCodePoint(codePoint: Int): Boolean =
    codePoint in MIN_SUPPLEMENTARY_CODE_POINT..MAX_CODE_POINT

private fun toCodePoint(highSurrogate: Char, lowSurrogate: Char): Int =
    (highSurrogate.code shl 10) + lowSurrogate.code + SURROGATE_DECODE_OFFSET

private fun isBasicMultilingualPlaneCodePoint(codePoint: Int): Boolean = codePoint ushr 16 == 0

private fun highSurrogateOf(codePoint: Int): Char =
    ((codePoint ushr 10) + HIGH_SURROGATE_ENCODE_OFFSET.code).toChar()

private fun lowSurrogateOf(codePoint: Int): Char =
    ((codePoint and 0x3FF) + MIN_LOW_SURROGATE.code).toChar()

private const val MAX_CODE_POINT: Int = 0x10FFFF

private const val MIN_SUPPLEMENTARY_CODE_POINT: Int = 0x10000

private const val SURROGATE_DECODE_OFFSET: Int =
    MIN_SUPPLEMENTARY_CODE_POINT - (MIN_HIGH_SURROGATE.code shl 10) - MIN_LOW_SURROGATE.code

private const val HIGH_SURROGATE_ENCODE_OFFSET: Char =
    MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT ushr 10)