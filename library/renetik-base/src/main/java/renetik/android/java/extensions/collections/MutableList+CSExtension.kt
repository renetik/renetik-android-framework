package renetik.android.java.extensions.collections

import renetik.android.java.common.tryOrNull

fun <T, ListType : MutableList<T>> ListType.put(item: T) = item.apply { add(item) }
fun <T, ListType : MutableList<T>> ListType.putAll(items: Iterable<T>) = apply { addAll(items) }
fun <T, ListType : MutableList<T>> ListType.putAll(vararg items: T) = apply { addAll(items) }
fun <T, ListType : MutableList<T>> ListType.put(item: T, index: Int) = item.apply { add(index, this) }
fun <T, ListType : MutableList<T>> ListType.replace(item: T, index: Int) = item.apply { set(index, item) }
fun <T, ListType : MutableList<T>> ListType.reload(values: Iterable<T>) = deleteAll().putAll(values)
fun <T, ListType : MutableList<T>> ListType.delete(item: T) = indexOf(item).apply { removeAt(this) }
fun <T, ListType : MutableList<T>> ListType.delete(index: Int): T? = at(index)?.apply { removeAt(index) }
fun <T, ListType : MutableList<T>> ListType.deleteFirst() = delete(0)
fun <T, ListType : MutableList<T>> ListType.deleteLast() = delete(lastIndex)
fun <T, ListType : MutableList<T>> ListType.deleteAll() = apply { clear() }
fun <T> MutableList<T>.removeRange(fromIndex: Int): List<T> =
    tryOrNull { range(fromIndex) }?.let {
        removeAll(it)
        return it
    } ?: let { return list() }
