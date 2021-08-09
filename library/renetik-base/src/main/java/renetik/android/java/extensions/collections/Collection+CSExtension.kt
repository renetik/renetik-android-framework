package renetik.android.java.extensions.collections

inline val Collection<*>.length get() = size

inline val Collection<*>.count get() = size

inline val Collection<*>.hasItems get() = size > 0

inline val Collection<*>.isEmpty get() = size == 0

inline val Collection<*>.lastIndex get() = size - 1

fun <T> Collection<T>.contains(vararg elements: T): Boolean {
    for (element in elements) if (!contains(element)) return false
    return true
}

fun <T> Collection<T>.containsNot(element: T) = !contains(element)
fun <T> Collection<T>.containsNot(vararg elements: T): Boolean {
    for (element in elements) if (!containsNot(element)) return false
    return true
}