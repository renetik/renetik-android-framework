package renetik.android.java.collections

import renetik.android.java.lang.CSValues
import renetik.android.lang.CSLang

fun <T> list(vararg items: T): CSList<T> {
    val list = list<T>()
    CSLang.add(list, *items)
    return list
}

fun <T> list(): CSList<T> {
    return CSListImpl()
}

fun <T> list(items: Iterable<T>?): CSList<T> {
    val list = list<T>()
    items?.forEach { list.put(it) }
    return list
}

fun <T> iterate(items: CSList<T>): CSIterator<T> {
    return CSListIterator(list(items))
}

interface CSList<T> : MutableList<T>, CSValues<T> {

    val hasItems: Boolean

    fun at(index: Int): T?

    fun delete(item: T): Int

    fun first(): T

    fun second(): T

    fun index(item: T): Int

    fun isLast(item: T): Boolean

    fun isLastIndex(index: Int): Boolean

    fun last(): T?

    fun previousLast(): T?

    fun count(): Int

    fun lastIndex(): Int

    fun rangeFrom(fromIndex: Int): CSList<T>

    fun range(fromIndex: Int, toIndex: Int): CSList<T>

    fun removeLast(): T

    fun removeAll(): CSList<T>

    fun put(item: T): T

    fun add(item: T, index: Int): T

    operator fun set(item: T, index: Int): T

    fun append(vararg item: T): CSList<T>

    fun append(items: Iterable<T>): CSList<T>

    fun insert(index: Int, item: T): CSList<T>

    fun reload(values: Iterable<T>): CSList<T>

    fun has(bow: T): Boolean

    fun length(): Int

    fun reverse(): CSList<T>

}
