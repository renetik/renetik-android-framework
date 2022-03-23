package renetik.android.framework.lang

import renetik.android.framework.lang.property.CSProperty
import renetik.android.primitives.Empty
import renetik.android.primitives.containsAll
import renetik.android.primitives.isFalse

inline val CSValue<Boolean>.isTrue get() = value
inline val CSValue<Boolean>.isFalse get() = !value

inline val CSValue<Int>.number get() = value
inline val CSValue<Int>.next get() = value + 1
inline val CSValue<Int>.previous get() = value - 1

inline val CSValue<Float>.number get() = value
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
        is Int -> value == Int.Empty
        is Float -> value == Float.Empty
        is Boolean -> value.isFalse
        else -> value == null
    }

fun CSValue<String>.contains(value: String,
                             ignoreCase: Boolean = false) =
    this.value.contains(value, ignoreCase)

fun CSValue<String>.contains(property: CSProperty<String>,
                             ignoreCase: Boolean = false) =
    this.contains(property.value, ignoreCase)

fun CSValue<String>.containsAll(words: List<String>,
                                ignoreCase: Boolean = false) =
    this.value.containsAll(words, ignoreCase)

inline val CSValue<Double>.number get() = value
