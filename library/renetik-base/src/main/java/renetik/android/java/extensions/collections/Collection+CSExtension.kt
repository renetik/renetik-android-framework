package renetik.android.java.extensions.collections

fun <T> Collection<T>.contains(vararg elements: T): Boolean {
    for (element in elements) if (!contains(element)) return false
    return true
}

fun <T> Collection<T>.containsNot(element: T) = !contains(element)
fun <T> Collection<T>.containsNot(vararg elements: T): Boolean {
    for (element in elements) if (!containsNot(element)) return false
    return true
}