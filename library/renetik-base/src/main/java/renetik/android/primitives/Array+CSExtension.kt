package renetik.android.primitives

import renetik.android.java.extensions.asString

inline fun <reified T> array(size: Int, default: T) = array(size) { _ -> default }
inline fun <reified T> array(size: Int, noinline create: (index: Int) -> T) = Array(size, create)
inline fun <reified T> array(size: Int, create: (index: Int, previous: T?) -> T): Array<T> {
    var previous: T? = null
    return Array(size) { index -> create(index, previous).apply { previous = this } }
}

val <T> Array<out T>.asStrings: Array<String>
    get() = map { it.asString }.toTypedArray()

inline val <T> Array<out T>.count: Int get() = size

val <T> Array<T>?.iterator: Iterator<T>
    get() = this?.iterator() ?: emptyList<T>().iterator()

val <T> Array<out T>.isEmpty get() = this.isEmpty()
val <T> Array<out T>.isSet get() = !this.isEmpty
fun <T> Array<out T>.ifEmpty(function: (Array<out T>) -> Unit) = apply {
    if (this.isEmpty) function(this)
}

fun <T> Array<out T>.ifSet(function: (Array<out T>) -> Unit) = apply {
    if (this.isSet) function(this)
}

inline val <reified T> Array<out T>.doubled
    get() = Array(size = size * 2) { index ->
        val valueIndex = if (index < size) index
        else index - size
        this[valueIndex]
    }

fun <T> Array<T>.range(fromIndex: Int, toIndex: Int) = copyOfRange(fromIndex, toIndex)

inline fun <reified T> Array<T>.extract(indexes: IntArray) = Array(indexes.size) { index ->
    this[indexes[index]]
}

