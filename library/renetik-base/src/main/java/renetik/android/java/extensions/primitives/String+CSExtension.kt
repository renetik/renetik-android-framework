package renetik.android.java.extensions.primitives

import renetik.android.java.common.catchWarnReturn
import renetik.android.java.common.catchWarnReturnNull
import renetik.android.java.extensions.*
import renetik.android.java.extensions.primitives.CSStringConstants.Empty
import renetik.android.java.extensions.primitives.CSStringConstants.NewLine
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.util.*

object CSStringConstants {
    const val NewLine = "\n"
    const val Comma = ","
    const val Semicolon = ";"
    const val Empty = ""

    fun randomString(length: Int): String {
        val random = Random()
        val letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val text = CharArray(length)
        for (i in 0 until length)
            text[i] = letters[random.nextInt(letters.length)]
        return String(text)
    }

    fun contains(string1: String?, string2: String?, ignoreCase: Boolean = false) =
        if (string1 == null || string2 == null) false
        else string1.contains(string2, ignoreCase)

    fun range(start: Int, endInclusive: Int): List<String> =
        IntRange(start, endInclusive).map { "$it" }
}

val String.isEmpty get() = size == 0

fun String.asLong() = catchWarnReturnNull<Long, NumberFormatException> { toLong() }
fun String.asFloat() = catchWarnReturnNull<Float, NumberFormatException> { toFloat() }
fun String.asInt() = catchWarnReturnNull<Int, NumberFormatException> { toInt() }
fun String.asDouble(default: Double) =
    catchWarnReturn<Double, NumberFormatException>(default) { toDouble() }

fun String.asLong(default: Long) =
    catchWarnReturn<Long, NumberFormatException>(default) { toLong() }

fun String.asFloat(default: Float) =
    catchWarnReturn<Float, NumberFormatException>(default) { toFloat() }

fun String.asInt(default: Int) =
    catchWarnReturn<Int, NumberFormatException>(default) { toInt() }

fun String.trimNewLines(): String {
    return replace(NewLine, Empty)
}

fun String.remove(toRemove: String): String {
    return replace(toRemove, "")
}

fun String.leaveEndOfLength(length: Int): String {
    if (this.length > length) {
        val toCut = this.length - length
        return substring(toCut)
    }
    return this
}

fun String.separateToString(vararg items: Any?): String {
    val text = StringBuilder()
    for (value in items) value.notNull { text.add(it).add(this) }
    if (!text.isEmpty) text.deleteLast(this.length)
    return text.toString()
}

val String.lowerCased: String get() = toLowerCase(Locale.ROOT)


val String.upperCased: String get() = toUpperCase(Locale.ROOT)

val String.noAccents: String get() = removeAccents()

private val nonSpacingCharactersRegex = "\\p{Mn}+".toRegex()
// some used this in remove accents code instead
//private val combiningdiacriticalMarksRegex = "\\p{Mn}+".toRegex()

fun String.removeAccents(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(nonSpacingCharactersRegex, "")
}

fun String.toMaxBytesSize(length: Int): String {
    val nameBytes = toByteArray(StandardCharsets.UTF_8)
    return if (nameBytes.size > length) {
        val bytes = ByteArray(length)
        System.arraycopy(nameBytes, 0, bytes, 0, length)
        String(bytes, StandardCharsets.UTF_8)
    } else this
}
