package renetik.android.core.kotlin.collections

inline fun <T> Iterable<T>.find(predicate: (T) -> Boolean): T? =
    firstOrNull(predicate)

inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean =
    find(predicate) != null

inline fun <T> Iterable<T>.forEachWithPrevious(function: (item: T, previous: T?) -> Unit) {
    var previous: T? = null
    for (item in this) {
        function(item, previous)
        previous = item
    }
}

inline fun <reified T> Iterable<*>.firstOfType(): T? =
    firstOrNull { it is T } as? T