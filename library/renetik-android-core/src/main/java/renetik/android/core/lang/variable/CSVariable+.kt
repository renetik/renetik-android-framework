@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.lang.variable

import renetik.android.core.lang.value.CSValue

inline infix fun <T> CSVariable<T>.assign(other: T) {
    value = other
}

inline infix fun <T> CSVariable<T>?.assign(other: T) = this?.assign(other)

inline infix fun <T> CSVariable<T>?.assign(other: CSValue<T>) {
    this?.assign(other.value)
}

inline fun <T> CSVariable<T?>.clear() = assign(null)

operator fun CSVariable<Float?>.plusAssign(value: Float) {
    this.value = this.value?.let { it + value }
}

operator fun CSVariable<Float?>.minusAssign(value: Float) {
    this.value = this.value?.let { it - value }
}

inline operator fun CSVariable<Float>.timesAssign(other: Float) {
    value *= other
}

inline operator fun CSVariable<Float>.divAssign(other: Float) {
    value /= other
}