package renetik.android.java.extensions.primitives

import renetik.android.java.extensions.asString
import renetik.android.java.extensions.collections.list

object ArrayCSExtension {
    fun <T> iterate(array: Array<T>?): Iterator<T> = array.iterator
}

fun <T> Array<out T>.asStrings() = list<String>().also { list ->
    for (index in indices) list.add(this[index].asString)
}.toTypedArray()

inline val <T> Array<out T>.count: Int get() = size

val <T> Array<T>?.iterator get() = this?.iterator() ?: emptyList<T>().iterator()

