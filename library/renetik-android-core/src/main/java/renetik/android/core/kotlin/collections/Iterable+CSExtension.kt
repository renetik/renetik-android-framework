package renetik.android.core.kotlin.collections

fun <T> Iterable<T>.forEachWithPrevious(function: (item: T, previous: T?) -> Unit) {
    var previous: T? = null
    for (item in this) {
        function(item, previous)
        previous = item
    }
}

inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean =
    find(predicate) != null