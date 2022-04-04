package renetik.kotlin.collections

fun <T> MutableCollection<T>.removeIf(filter: (T) -> Boolean) =
    iterator().removeIf(filter)

fun <K, V> MutableMap<K, V>.removeIf(filter: (K, V) -> Boolean) =
    iterator().removeIf { filter(it.key, it.value) }

fun <T> MutableIterator<T>.removeIf(filter: (T) -> Boolean): Boolean {
    for (item in this) if (filter.invoke(item)) {
        remove()
        return true
    }
    return false
}