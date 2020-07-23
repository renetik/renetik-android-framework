package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.*
import java.util.*

val emptyString get() = ""

fun generateRandomStringOfLength(length: Int): String {
    val random = Random()
    val letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val text = CharArray(length)
    for (i in 0 until length)
        text[i] = letters[random.nextInt(letters.length)]
    return String(text)
}

fun containsNoCase(string1: String, string2: String) =
    if (string1.isNull || string2.isNull) false
    else string1.lowerCased.contains(string2.lowerCased)

fun String.asLong(): Long? {
    return try {
        toLong()
    } catch (ex: NumberFormatException) {
        null
    }
}

fun String.asFloat(): Float? {
    return try {
        toFloat()
    } catch (ex: NumberFormatException) {
        null
    }
}

fun String.asInt(): Int? {
    return try {
        toInt()
    } catch (ex: NumberFormatException) {
        null
    }
}

fun String.asDouble(default: Double): Double {
    return try {
        toDouble()
    } catch (ex: NumberFormatException) {
        default
    }
}

fun String.asLong(default: Long): Long {
    return try {
        toLong()
    } catch (ex: NumberFormatException) {
        default
    }
}

fun String.asFloat(default: Float): Float {
    return try {
        toFloat()
    } catch (ex: NumberFormatException) {
        default
    }
}

fun String.asInt(default: Int): Int {
    return try {
        toInt()
    } catch (ex: NumberFormatException) {
        default
    }
}

fun String.trimNewLines(): String {
    return replace("\n", "")
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

fun String.separateToString(items: Iterable<Any?>?) = items?.let {
    val text = StringBuilder()
    for (value in items) value.notNull { text.add(it).add(this) }
    if (!text.isEmpty) text.deleteLast(this.length)
    text.toString()
} ?: ""

fun String.separateToString(vararg items: Any?): String {
    val text = StringBuilder()
    for (value in items) value.notNull { text.add(it).add(this) }
    if (!text.isEmpty) text.deleteLast(this.length)
    return text.toString()
}

val String.lowerCased: String get() = toLowerCase(Locale.ROOT)