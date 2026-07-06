@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.lang.value

import renetik.android.core.kotlin.primitives.containsAll
import renetik.android.core.lang.value.CSValue.Companion.value
import renetik.android.core.lang.variable.CSVariable

inline infix fun <T> T?.equalOrNull(other: T): Boolean = this == null || this == other
inline infix fun <T> T.equal(other: CSValue<T>): Boolean = this == other.value
inline infix fun <T> T.equalNot(other: CSValue<T>): Boolean = this != other.value
inline infix fun <T> CSValue<T>.equal(other: T): Boolean = value == other
inline infix fun <T> CSValue<T>.equalNot(other: T): Boolean = value != other

inline fun <T> CSValue<T>.equalAny(vararg items: T?): Boolean = value in items

@JvmName("equalNotNullable")
inline infix fun <T> CSValue<T>.equalNot(other: T?): Boolean = value != other
inline infix fun <T> CSValue<T>.equal(other: CSValue<T>): Boolean = value == other.value
inline infix fun <T> CSValue<T>.equalNot(other: CSValue<T>): Boolean = value != other.value

operator fun CSValue<Boolean>.not() = value(!value)

inline val <T> CSValue<T?>.isNull get() = value == null
inline val <T> CSValue<T?>.notNull get() = value != null

inline val CSValue<Int>.number get() = value
inline val CSValue<Int>.next get() = value + 1
inline val CSValue<Int>.previous get() = value - 1

inline val CSValue<Float>.number get() = value
inline val CSValue<Double>.number get() = value

@get:JvmName("CSValueCharSequenceIsEmpty")
inline val CSValue<out CharSequence>.isEmpty get() = value.isEmpty()

fun CSValue<out CharSequence>.contains(
    value: String, ignoreCase: Boolean = false
) = this.value.contains(value, ignoreCase)

fun CSValue<out CharSequence>.contains(
    property: CSVariable<String>, ignoreCase: Boolean = false
) = this.contains(property.value, ignoreCase)

fun CSValue<out CharSequence>.containsAll(
    words: List<String>, ignoreCase: Boolean = false
) = value.containsAll(words, ignoreCase)