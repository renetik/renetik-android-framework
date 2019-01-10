package renetik.android.java.extensions

import java.util.*

fun Throwable.getRootCauseMessage() = getRootCause()?.message ?: ""

fun Throwable.getRootCause(): Throwable? {
    var throwable: Throwable? = this
    val list = ArrayList<Throwable>()
    while (throwable != null && !list.contains(throwable)) {
        list.add(throwable)
        throwable = throwable.cause
    }
    return if (list.size < 2) null else list[list.size - 1]
}
