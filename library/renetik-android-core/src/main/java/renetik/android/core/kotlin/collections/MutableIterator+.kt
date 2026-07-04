package renetik.android.core.kotlin.collections

fun <T> MutableIterator<T>.removeFirst(condition: (T) -> Boolean): Boolean {
    for (item in this) if (condition.invoke(item)) {
        remove()
        return true
    }
    return false
}

fun <T> MutableIterator<T>.removeAll(condition: (T) -> Boolean) {
    for (item in this) if (condition.invoke(item)) remove()
}