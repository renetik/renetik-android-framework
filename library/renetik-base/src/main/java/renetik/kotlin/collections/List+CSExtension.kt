package renetik.kotlin.collections

import renetik.android.framework.lang.CSList

val <T> List<T>.first get() = at(0)

val <T> List<T>.second get() = at(1)

val <T> List<T>.third get() = at(2)

val <T> List<T>.beforeLast get() = at(lastIndex - 1)

val <T> List<T>.last get() = at(lastIndex)

fun <T> List<T>.at(index: Int): T? = if (index in 0 until size) get(index) else null

fun <T> List<T>.index(item: T): Int? {
    val indexOf = indexOf(item)
    return if (indexOf == -1) null else indexOf
}

fun <T> List<T>.index(predicate: (T) -> Boolean): Int? {
    val indexOf = indexOfFirst(predicate)
    return if (indexOf == -1) null else indexOf
}

fun <T> List<T>.has(item: T) = contains(item)
fun <T> List<T>.hasAll(items: List<T>): Boolean = containsAll(items)
fun <T> List<T>.hasAll(items: Array<out T>): Boolean = containsAll(items.asList())
fun <T> List<T>.hasAll(items: Iterable<T>): Boolean = containsAll(items.toList())


fun <T> List<T>.second() = this[1]

fun <T> List<T>.third() = this[2]

fun <T> List<T>.beforeLast() = this[lastIndex - 1]

fun <T> List<T>.isLast(item: T) = last === item

fun <T> List<T>.isLastIndex(index: Int) = index == lastIndex

fun <T> List<T>.range(fromIndex: Int) = range(fromIndex, size)

fun <T> List<T>.range(fromIndex: Int, toIndex: Int) =
    list(subList(fromIndex, toIndex))


fun <T> list(block: (MutableList<T>.() -> Unit)? = null): CSList<T> =
    CSList<T>().apply { block?.invoke(this) }

fun <T> list(size: Int): MutableList<T> = ArrayList(size)
fun <T> listOfNulls(size: Int) = list<T?>(size, null)
fun <T> list(size: Int, default: T) = MutableList(size) { default }
fun <T> list(size: Int, init: (index: Int) -> T) = MutableList(size, init)
fun <T> list(vararg items: T): MutableList<T> = list<T>().putAll(*items)
fun <T> list(items: Iterable<T>): MutableList<T> = list<T>().putAll(items)
fun <T> list(items: Collection<T>): MutableList<T> = CSList(items)
fun <T> list(vararg items: Iterable<T>): MutableList<T> = list<T>().also {
    for (iterable in items) it.addAll(iterable)
}

inline fun <reified T> list(
    size: Int, create: (index: Int, previous: T?, size: Int) -> T): List<T> {
    var previous: T? = null
    return List(size) { index -> create(index, previous, size).apply { previous = this } }
}

fun <T, A, B> combine(
    arrayA: Array<A>, arrayB: Array<B>, createItem: (A, B) -> T) =
    combine(arrayA.asList(), arrayB.asList(), createItem)

fun <T, A, B> combine(
    collectionA: Collection<A>, collectionB: Collection<B>, createItem: (A, B) -> T) =
    list<T>(size = collectionA.size * collectionB.size).apply {
        for (a in collectionA) for (b in collectionB) add(createItem(a, b))
    }
