package renetik.android.java.extensions.collections

val <T> List<T>.length get() = size

val <T> List<T>.hasItems get() = size > 0

val <T> List<T>.isEmpty get() = size == 0

val <T> List<T>.lastIndex get() = size - 1

val <T> List<T>.first get() = at(0)

val <T> List<T>.second get() = at(1)

val <T> List<T>.third get() = at(2)

val <T> List<T>.beforeLast get() = at(lastIndex - 1)

val <T> List<T>.last get() = at(lastIndex)

fun <T> List<T>.second() = this[1]

fun <T> List<T>.third() = this[2]

fun <T> List<T>.beforeLast() = this[lastIndex - 1]

fun <T> List<T>.at(index: Int): T? = if (index in 0..(size - 1)) get(index) else null

fun <T> List<T>.isLast(item: T) = last === item

fun <T> List<T>.isLastIndex(index: Int) = index == lastIndex

fun <T> List<T>.range(fromIndex: Int) = range(fromIndex, size)

fun <T> List<T>.range(fromIndex: Int, toIndex: Int): List<T> {
    return list(subList(fromIndex, toIndex))
}

fun <T> List<T>.has(item: T) = contains(item)

fun <T> list(): MutableList<T> = ArrayList()
fun <T> list(vararg items: T): MutableList<T> = list<T>().putAll(*items)
fun <T> list(items: Iterable<T>): MutableList<T> = list<T>().putAll(items)