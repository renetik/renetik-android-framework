package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.*
import renetik.android.java.extensions.primitives.CSStringConstants.Empty
import renetik.android.java.extensions.primitives.CSStringConstants.NewLine
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

fun String.asLong(): Long? = try {
    toLong()
} catch (ex: NumberFormatException) {
    null
}

fun String.asFloat(): Float? = try {
    toFloat()
} catch (ex: NumberFormatException) {
    null
}

fun String.asInt(): Int? = try {
    toInt()
} catch (ex: NumberFormatException) {
    null
}

fun String.asDouble(default: Double): Double = try {
    toDouble()
} catch (ex: NumberFormatException) {
    default
}

fun String.asLong(default: Long): Long = try {
    toLong()
} catch (ex: NumberFormatException) {
    default
}

fun String.asFloat(default: Float): Float = try {
    toFloat()
} catch (ex: NumberFormatException) {
    default
}

fun String.asInt(default: Int): Int = try {
    toInt()
} catch (ex: NumberFormatException) {
    default
}

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
