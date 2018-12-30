package renetik.android.java.collections

import renetik.android.java.common.CSValues
import renetik.android.java.extensions.collections.putAll

fun <T> list(): MutableList<T> = mutableListOf()

fun <T> list(vararg items: T): MutableList<T> = list<T>().putAll(*items)

fun <T> list(items: Iterable<T>): MutableList<T> = list<T>().putAll(items)

//interface CSList<T> : MutableList<T>, List<T>, CSValues<T> {

//    val hasItems: Boolean
//    fun at(index: Int): T?
//    fun isLast(item: T): Boolean
//    fun isLastIndex(index: Int): Boolean
////    val lastIndex: Int
//    fun rangeFrom(fromIndex: Int): CSList<T>
//    fun range(fromIndex: Int, toIndex: Int): CSList<T>
//
//    fun put(item: T): T
//    fun put(item: T, index: Int): T
//    fun putAll(vararg items: T): CSList<T>
//    fun putAll(items: Iterable<T>): CSList<T>
//    fun replace(item: T, index: Int): T
//    fun reload(values: Iterable<T>): CSList<T>
//
//    fun reverse(): CSList<T>
//
//    fun delete(index: Int): T?
//    fun delete(item: T): Int
//    fun deleteLast(): T?
//    fun deleteAll(): CSList<T>

//}
