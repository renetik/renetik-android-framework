@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin.collections


inline val Collection<*>.length get() = size

inline val Collection<*>.count get() = size

inline val Collection<*>.hasItems get() = isNotEmpty()

inline val Collection<*>.lastIndex get() = size - 1

infix fun <T> Collection<T>.hasAll(items: List<T>): Boolean = containsAll(items)
infix fun <T> Collection<T>.hasAll(items: Array<out T>): Boolean =
    containsAll(items.asList())

infix fun <T> Collection<T>.hasAll(items: Iterable<T>): Boolean =
    containsAll(items.toList())

infix fun <T> Collection<T>.hasNot(element: T) = !contains(element)

fun <T> Collection<T>.hasNot(vararg elements: T): Boolean {
    for (element in elements) if (!hasNot(element)) return false
    return true
}

fun <T> Collection<T>.index(item: T): Int? = indexOf(item).takeIf { it >= 0 }

fun <T> Collection<T>.firstIndex(predicate: (T) -> Boolean): Int? =
    indexOfFirst(predicate).takeIf { it >= 0 }

fun <T> Collection<T>.lastIndex(predicate: (T) -> Boolean): Int? =
    indexOfLast(predicate).takeIf { it >= 0 }

inline fun <T> Collection<T>.mutable(): MutableList<T> = toMutableList()
