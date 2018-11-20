package renetik.java.extensions.collections

import renetik.java.collections.CSList
import renetik.java.collections.CSListImpl

fun <T> List<T>.at(index: Int): T? {
    return if (index in 0..(size - 1)) get(index) else null
}

fun <T> List<T>.firstItem(): T? {
    return at(0)
}

fun <T> List<T>.secondItem(): T? {
    return at(1)
}

fun <T> List<T>.thirdItem(): T? {
    return at(1)
}

fun <T> List<T>.index(item: T): Int {
    return indexOf(item)
}

fun <T> List<T>.getHasItems(): Boolean {
    return size > 0
}

fun <T> List<T>.isLast(item: T): Boolean {
    return lastItem() === item
}

fun <T> List<T>.previousLastItem(): T? {
    return at(lastIndex() - 1)
}

fun <T> List<T>.lastItem(): T? {
    return at(lastIndex())
}

fun <T> List<T>.lastIndex(): Int {
    return size - 1
}

fun <T> List<T>.range(fromIndex: Int): CSList<T> {
    return range(fromIndex, size)
}

fun <T> List<T>.range(fromIndex: Int, toIndex: Int): CSList<T> {
    return CSListImpl<T>(subList(fromIndex, toIndex))
}

fun <T> List<T>.count(): Int {
    return size
}

fun <T> List<T>.has(item: T): Boolean {
    return contains(item)
}

fun <T> List<T>.length(): Int {
    return size
}