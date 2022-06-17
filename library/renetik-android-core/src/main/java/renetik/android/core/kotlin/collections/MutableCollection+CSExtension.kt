package renetik.android.core.kotlin.collections

fun <T> MutableCollection<T>.removeIf(condition: (T) -> Boolean) =
    iterator().removeIf(condition)

fun <K, V> MutableMap<K, V>.removeIf(condition: (K, V) -> Boolean) =
    iterator().removeIf { condition(it.key, it.value) }

//This is actually remove first !! in iOS implemented as remove all ...
fun <T> MutableIterator<T>.removeIf(condition: (T) -> Boolean): Boolean {
    for (item in this) if (condition.invoke(item)) {
        remove()
        return true
    }
    return false
}