package renetik.java.collections

import renetik.java.lang.CSValues

fun <T> list(): CSList<T> = CSListImpl<T>()

fun <T> list(vararg items: T): CSList<T> = list<T>().putAll(*items)

fun <T> list(items: Iterable<T>): CSList<T> = list<T>().putAll(items)

interface CSList<T> : MutableList<T>, List<T>, CSValues<T> {

    val hasItems: Boolean

    fun at(index: Int): T?
    fun first(): T?
    fun second(): T?
    fun last(): T?
    fun previousLast(): T?

    fun isLast(item: T): Boolean
    fun isLastIndex(index: Int): Boolean

    fun lastIndex(): Int

    fun rangeFrom(fromIndex: Int): CSList<T>
    fun range(fromIndex: Int, toIndex: Int): CSList<T>

    fun put(item: T): T
    fun put(item: T, index: Int): T
    fun putAll(vararg items: T): CSList<T>
    fun putAll(items: Iterable<T>): CSList<T>
    fun replace(item: T, index: Int): T
    fun reload(values: Iterable<T>): CSList<T>

    fun reverse(): CSList<T>

    fun delete(index: Int): T?
    fun delete(item: T): Int
    fun deleteLast(): T?
    fun deleteAll(): CSList<T>

}
