package renetik.android.core.kotlin.sequences

fun <T> Sequence<T>.findNextAfter(item: T): T? {
    var index = 0
    var itemFound = false
    for (sequenceItem in this) {
        if (itemFound) return sequenceItem
        else if (sequenceItem == item) itemFound = true
        index++
    }
    return null
}

fun <T> Sequence<T>.firstIndexOf(item: T): Int? =
    firstIndexOf { it == item }

fun <T> Sequence<T>.firstIndexOf(predicate: (item: T) -> Boolean): Int? =
    indexOfFirst(predicate).let { if (it == -1) null else it }