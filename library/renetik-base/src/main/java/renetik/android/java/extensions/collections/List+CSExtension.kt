package renetik.android.java.extensions.collections

import renetik.android.java.collections.list

val <T> List<T>.length get() = size

val <T> List<T>.hasItems get() = size > 0

val <T> List<T>.lastIndex get() = size - 1

val <T> List<T>.firstItem get() = at(0)

val <T> List<T>.secondItem get() = at(1)

val <T> List<T>.thirdItem get() = at(2)

val <T> List<T>.previousLastItem get() = at(lastIndex - 1)

val <T> List<T>.lastItem get() = at(lastIndex)

fun <T> List<T>.at(index: Int): T? = if (index in 0..(size - 1)) get(index) else null

fun <T> List<T>.isLast(item: T) = lastItem === item

fun <T> List<T>.isLastIndex(index: Int) = index == lastIndex

fun <T> List<T>.range(fromIndex: Int) = range(fromIndex, size)

fun <T> List<T>.range(fromIndex: Int, toIndex: Int) = list(subList(fromIndex, toIndex))

fun <T> List<T>.has(item: T) = contains(item)

