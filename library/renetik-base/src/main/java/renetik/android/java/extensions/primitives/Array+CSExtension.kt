package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.stringify

fun <T> Array<out T>.asStrings() = list<String>().apply {
    for (index in indices) add(this@asStrings[index].stringify())
}.toTypedArray()

inline val <T> Array<out T>.count: Int get() = size

val <T> Array<T>?.iterator get() = this?.iterator() ?: listOf<T>().iterator()