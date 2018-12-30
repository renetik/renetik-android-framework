package renetik.android.java.extensions.collections

import java.util.*

fun <T> MutableList<T>.put(item: T): T {
    add(item)
    return item
}

fun <T> MutableList<T>.putAll(items: Iterable<T>) = apply { for (item in items) add(item) }

fun <T> MutableList<T>.putAll(vararg items: T) = putAll(Arrays.asList(*items))

fun <T> MutableList<T>.put(item: T, index: Int): T {
    add(index, item)
    return item
}

fun <T> MutableList<T>.replace(item: T, index: Int): T {
    set(index, item)
    return item
}

fun <T> MutableList<T>.reload(values: Iterable<T>) = deleteAll().putAll(values)

fun <T> MutableList<T>.delete(item: T): Int {
    val index = indexOf(item)
    removeAt(index)
    return index
}

fun <T> MutableList<T>.delete(index: Int): T? = at(index)?.apply { removeAt(index) }

fun <T> MutableList<T>.deleteFirst() = delete(0)

fun <T> MutableList<T>.deleteLast() = delete(lastIndex)

fun <T> MutableList<T>.deleteAll() = apply { clear() }