package renetik.android.java.extensions.collections

import renetik.android.java.common.tryOrNull

fun <T, ListType : MutableList<T>> ListType.put(item: T) = item.apply { add(item) }
fun <T, ListType : MutableList<T>> ListType.putAll(items: Iterable<T>) = apply { addAll(items) }

//fun <T, ListType : MutableList<T>> ListType.putAll(items: Array<T>) = apply { items?.let { for(item in items) add(item) }  }
fun <T, ListType : MutableList<T>> ListType.putAll(vararg items: T) = apply { addAll(items) }
fun <T, ListType : MutableList<T>> ListType.putAllDistinct(other: List<T>) = apply {
//    other.forEach { item -> if (!contains(item)) add(item) }
    reload(putAll(other).distinct())
}

fun <T, ListType : MutableList<T>> ListType.put(item: T, index: Int) =
    item.apply { add(index, this) }

fun <T, ListType : MutableList<T>> ListType.replace(item: T, index: Int) =
    item.apply { set(index, item) }

fun <T, ListType : MutableList<T>> ListType.reload(values: Iterable<T>) = deleteAll().putAll(values)
fun <T, ListType : MutableList<T>> ListType.delete(item: T): T {
    remove(item)
    return item
}

fun <T, ListType : MutableList<T>> ListType.delete(index: Int): T? =
    at(index)?.apply { removeAt(index) }

fun <T, ListType : MutableList<T>> ListType.deleteFirst() = delete(0)
fun <T, ListType : MutableList<T>> ListType.deleteLast() = delete(lastIndex)
fun <T, ListType : MutableList<T>> ListType.deleteAll() = apply { clear() }
fun <T> MutableList<T>.removeRange(fromIndex: Int): List<T> =
    tryOrNull { range(fromIndex) }?.let {
        removeAll(it)
        return it
    } ?: let { return list() }

fun <T> MutableList<T>.deleteFirst(filter: (T) -> Boolean): T? {
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

fun <T> MutableList<T>.deleteLast(filter: (T) -> Boolean): T? {
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

fun <T> MutableList<T>.deleteIf(filter: (T) -> Boolean): Boolean {
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
