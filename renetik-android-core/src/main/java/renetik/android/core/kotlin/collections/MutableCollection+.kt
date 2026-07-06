package renetik.android.core.kotlin.collections

fun <T> MutableCollection<T>.removeFirst(condition: (T) -> Boolean) =
    iterator().removeFirst(condition)