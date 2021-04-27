package renetik.android.primitives

import renetik.android.java.extensions.asString

val <T> Array<out T>.asStrings: Array<String>
    get() = map { it.asString }.toTypedArray()

inline val <T> Array<out T>.count: Int get() = size

val <T> Array<T>?.iterator: Iterator<T>
    get() = this?.iterator() ?: emptyList<T>().iterator()

