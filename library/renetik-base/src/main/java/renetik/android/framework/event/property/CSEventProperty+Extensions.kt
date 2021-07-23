package renetik.android.framework.event.property

import renetik.android.java.extensions.asString
import renetik.android.primitives.empty
import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue


fun CSEventProperty<Int>.next() = value + 1
fun CSEventProperty<Int>.previous() = value - 1

fun CSEventProperty<Boolean>.toggle() = apply { value = !value }
fun CSEventProperty<Boolean>.setFalse() = apply { value = false }
fun CSEventProperty<Boolean>.setTrue() = apply { value = true }
fun CSEventProperty<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventProperty<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }

inline var CSEventProperty<Boolean>.isTrue
    get() = value
    set(newValue) {
        value = newValue
    }
inline var CSEventProperty<Boolean>.isFalse
    get() = !value
    set(newValue) {
        value = !newValue
    }

inline var CSEventProperty<String?>.string
    get() = value.asString
    set(newValue) {
        value = newValue
    }

inline val CSEventProperty<Float>.isEmpty get() = value == Float.empty
inline val CSEventProperty<Float>.isSet get() = !this.isEmpty
inline fun CSEventProperty<Float>.ifEmpty(function: (CSEventProperty<Float>) -> Unit) = apply {
    if (this.isEmpty) function(this)
}

inline fun CSEventProperty<Float>.ifSet(function: (CSEventProperty<Float>) -> Unit) = apply {
    if (this.isSet) function(this)
}