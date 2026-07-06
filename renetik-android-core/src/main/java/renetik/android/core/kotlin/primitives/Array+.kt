@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.kotlin.primitives

import renetik.android.core.kotlin.asString

val emptyFloatArray = floatArrayOf()

fun <T> Array<T?>.remove(index: Int) = this[index]?.also { this[index] = null }
fun <T> Array<out T?>.forEachSet(action: (T) -> Unit) = forEach { it?.also(action) }
fun <T> Array<T?>.clear() = indices.forEach { this[it] = null }

inline infix fun <T> Array<T>.allIn(other: List<T>): Boolean = all { it in other }
inline infix fun <T> Array<T>.anyIn(other: List<T>): Boolean = any { it in other }
inline infix fun <T> Array<T>.noneIn(other: List<T>): Boolean = none { it in other }
inline infix fun <reified T> T.with(other: T): Array<T> = arrayOf(this, other)

inline fun <reified T> array(size: Int, default: T) = array(size) { _ -> default }
inline fun <reified T> array(size: Int, noinline create: (index: Int) -> T) = Array(size, create)
inline fun <reified T> array(size: Int, noinline create: (index: Int, size: Int) -> T) =
    Array(size) { index -> create(index, size) }

inline fun <reified T> array(
    size: Int, create: (index: Int, previous: T?, size: Int) -> T
): Array<T> {
    var previous: T? = null
    return Array(size) { index -> create(index, previous, size).apply { previous = this } }
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

fun <T> Array<T>.range(range: IntRange) = copyOfRange(range.first, range.last + 1)

fun <T> Array<T>.range(fromIndex: Int, length: Int) = range(fromIndex until fromIndex + length)

inline fun <reified T> Array<T>.extract(indexes: IntArray) = Array(indexes.size) { index ->
    this[indexes[index]]
}

inline fun <reified T> Array<out T>.toArray() = asList().toTypedArray()

fun <T> Array<out T>.at(index: Int): T? = if (index in indices) get(index) else null

inline fun <T> Array<T>.forEachReverse(action: (T) -> Unit) {
    var index = lastIndex
    while (index >= 0) {
        action(this[index])
        index--
    }
}

