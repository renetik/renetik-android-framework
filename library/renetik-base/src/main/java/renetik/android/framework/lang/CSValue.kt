package renetik.android.framework.lang

import renetik.android.primitives.empty
import renetik.android.primitives.isFalse

interface CSValue<T> {
    val value: T

    companion object {
        fun <T> value(value: T) = object : CSValue<T> {
            override val value: T = value
        }
    }
}

inline val CSValue<Float>.isSet get() = !isEmpty
inline fun CSValue<Float>.ifEmpty(function: (CSValue<Float>) -> Unit) = apply {
    if (isEmpty) function(this)
}

inline fun CSValue<Float>.ifSet(function: (CSValue<Float>) -> Unit) = apply {
    if (this.isSet) function(this)
}

val CSValue<*>.isEmpty
    get() = when (val value = value) {
        is CharSequence -> value.isEmpty()
        is Int -> value == Int.empty
        is Float -> value == Float.empty
        is Boolean -> value.isFalse
        else -> value == null
    }

fun CSValue<String>.contains(value: String, ignoreCase: Boolean = false) =
    this.value.contains(value, ignoreCase)

fun CSValue<String>.contains(property: CSProperty<String>, ignoreCase: Boolean = false) =
    this.contains(property.value, ignoreCase)

inline val CSValue<Int>.number get() = value
inline val CSValue<Float>.number get() = value
inline val CSValue<Double>.number get() = value