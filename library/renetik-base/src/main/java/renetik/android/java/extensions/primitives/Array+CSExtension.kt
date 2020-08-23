package renetik.android.java.extensions.primitives

import renetik.android.java.common.CSName
import renetik.android.java.extensions.collections.list

object ArrayCSExtension {
    fun <T> iterate(array: Array<T>?): Iterator<T> = array.iterator
}

fun <T> Array<out T>.asStrings() = list<String>().apply {
    for (index in indices) {
        val t = this@asStrings[index]
        val name = t as? CSName
        add(name?.name ?: t?.toString() ?: "")
    }
}.toTypedArray()

inline val <T> Array<out T>.count: Int get() = size

val <T> Array<T>?.iterator get() = this?.iterator() ?: emptyList<T>().iterator()

