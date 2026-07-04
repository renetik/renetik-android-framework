@file:Suppress("unused", "NOTHING_TO_INLINE")

package renetik.android.core.kotlin.collections

import renetik.android.core.lang.catchAllWarnReturnNull

fun <T> mutableListOf(size: Int): MutableList<T> = ArrayList(size)

inline fun <T> MutableList<T>.clone(): MutableList<T> = toMutableList()

operator fun <T> MutableList<T>.plus(other: T): MutableList<T> =
    apply { add(other) }

fun <T, ListType : MutableList<T>> ListType.put(item: T) =
    item.apply { add(item) }

fun <T, ListType : MutableList<T>> ListType.putAll(items: Iterable<T>) =
    apply { addAll(items) }

fun <T, ListType : MutableList<T>> ListType.putAll(vararg items: T) =
    apply { addAll(items) }

fun <T, ListType : MutableList<T>> ListType.addDistinct(items: List<T>) =
    apply { items.forEach { addDistinct(it) } }

fun <T, ListType : MutableList<T>> ListType.addDistinct(item: T): T =
    item.apply { find { it == item } ?: add(item) }

fun <T, ListType : MutableList<T>> ListType.replaceEqual(items: List<T>) =
    apply { items.forEach { replaceEqual(it) } }

fun <T, ListType : MutableList<T>> ListType.replaceEqual(item: T) = item.also {
    val index = indexOf(it); if (index > -1) set(index, it) else add(it)
}

fun <T, ListType : MutableList<T>> ListType.put(item: T, index: Int): T =
    item.apply { add(index, this) }

fun <T, ListType : MutableList<T>> ListType.replace(item: T, index: Int) =
    item.apply { set(index, item) }

fun <T, ListType : MutableList<T>> ListType.set(item: T, index: Int) =
    item.apply { set(index, item) }

infix fun <T, ListType : MutableList<T>> ListType.reload(values: Iterable<T>) =
    deleteAll().putAll(values)

fun <T, ListType : MutableList<T>> ListType.delete(item: T): T =
    item.also { remove(it) }

fun <T, ListType : MutableList<T>> ListType.delete(index: Int): T? =
    at(index)?.apply { removeAt(index) }

fun <T, ListType : MutableList<T>> ListType.deleteFirst() = delete(0)
fun <T, ListType : MutableList<T>> ListType.deleteLast() = delete(lastIndex)
fun <T, ListType : MutableList<T>> ListType.deleteAll() = apply { clear() }
fun <T> MutableList<T>.removeRange(fromIndex: Int): List<T> =
    catchAllWarnReturnNull { rangeFrom(fromIndex) }?.let {
        removeAll(it)
        return it
    } ?: let { return mutableListOf() }

inline fun <T> MutableList<T>.delete(filter: (T) -> Boolean) = deleteFirst(filter)

inline fun <T> MutableList<T>.deleteFirst(filter: (T) -> Boolean): T? {
    val each = iterator()
    while (each.hasNext()) {
        val item = each.next()
        if (filter(item)) {
            each.remove()
            return item
        }
    }
    return null
}

inline fun <T> MutableList<T>.deleteLast(filter: (T) -> Boolean): T? {
    val each = listIterator(size)
    while (each.hasPrevious()) {
        val item = each.previous()
        if (filter(item)) {
            each.remove()
            return item
        }
    }
    return null
}

inline fun <T> MutableList<T>.deleteIf(filter: (T) -> Boolean): Boolean {
    var removed = false
    val each = iterator()
    while (each.hasNext()) {
        if (filter(each.next())) {
            each.remove()
            removed = true
        }
    }
    return removed
}

inline fun <T, R : Comparable<R>> MutableList<T>.sortedWith(
    crossinline using: (T) -> R?
) = apply { sortBy(using) }

fun <T> MutableList<T>.rangeFrom(index: Int): MutableList<T> = range(index, size)
fun <T> MutableList<T>.rangeTo(index: Int): MutableList<T> = range(0, index)
fun <T> MutableList<T>.range(fromIndex: Int, toIndex: Int): MutableList<T> =
    subList(fromIndex, toIndex)

fun <T> MutableList<T>.swap(first: Int, second: Int) {
    val tmp = this[first]
    this[first] = this[second]
    this[second] = tmp
}

fun MutableList<Int>.flipRangeInPlace(count: Int) {
    val values = filter { it in 0 until count }.asReversed()
    var idx = 0
    for (i in indices) {
        if (this[i] in 0 until count) {
            this[i] = values[idx++]
        }
    }
}
