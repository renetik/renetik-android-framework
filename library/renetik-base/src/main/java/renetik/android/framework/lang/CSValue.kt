package renetik.android.framework.lang

import renetik.android.primitives.empty

interface CSValue<T> {
    val value: T
}

inline val CSValue<Float>.isEmpty get() = value == Float.empty
inline val CSValue<Float>.isSet get() = !this.isEmpty
inline fun CSValue<Float>.ifEmpty(function: (CSValue<Float>) -> Unit) = apply {
    if (this.isEmpty) function(this)
}

inline fun CSValue<Float>.ifSet(function: (CSValue<Float>) -> Unit) = apply {
    if (this.isSet) function(this)
}