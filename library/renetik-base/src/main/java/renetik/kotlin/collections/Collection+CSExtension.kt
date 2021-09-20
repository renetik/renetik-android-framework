package renetik.kotlin.collections

inline val Collection<*>.length get() = size

inline val Collection<*>.count get() = size

inline val Collection<*>.hasItems get() = isNotEmpty()

inline val Collection<*>.isEmpty get() = isEmpty()

inline val Collection<*>.lastIndex get() = size - 1

fun <T> Collection<T>.hasAll(vararg elements: T): Boolean {
    for (element in elements) if (!contains(element)) return false
    return true
}

fun <T> Collection<T>.containsNot(element: T) = !contains(element)
fun <T> Collection<T>.containsNot(vararg elements: T): Boolean {
    for (element in elements) if (!containsNot(element)) return false
    return true
}