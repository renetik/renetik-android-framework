package renetik.android.java.collections

import renetik.android.java.common.CSValues
import renetik.android.java.extensions.collections.putAll

fun <T> list(): CSList<T> = CSList()

fun <T> list(vararg items: T): CSList<T> = list<T>().putAll(*items)

fun <T> list(items: Iterable<T>): CSList<T> = list<T>().putAll(items)

class CSList<T> : ArrayList<T>(), CSValues<T> {
    override val values get() = this
}