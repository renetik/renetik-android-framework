package renetik.android.framework.lang

import renetik.android.java.extensions.asString

interface CSProperty<T> : CSValue<T> {
    override var value: T
}

fun <T> CSProperty<T>.value(value: T) = apply { this.value = value }

fun CSProperty<Boolean>.setTrue() = apply { this.value = true }
fun CSProperty<Boolean>.setFalse() = apply { this.value = false }
fun CSProperty<Boolean>.toggle() = apply { value = !value }
inline var CSProperty<Boolean>.isTrue
    get() = value
    set(newValue) {
        value = newValue
    }
inline var CSProperty<Boolean>.isFalse
    get() = !value
    set(newValue) {
        value = !newValue
    }


val CSProperty<String>.isEmpty get() = value.isEmpty()
fun CSProperty<String>.contains(value: String, ignoreCase: Boolean = false) =
    this.value.contains(value, ignoreCase)
fun CSProperty<String>.contains(property: CSProperty<String>, ignoreCase: Boolean = false) =
    this.contains(property.value, ignoreCase)

inline var CSProperty<String?>.string
    get() = value.asString
    set(newValue) {
        value = newValue
    }

fun CSProperty<Int>.increment() = apply { value++ }
fun CSProperty<Int>.decrement() = apply { value-- }
fun CSProperty<Int>.next() = value + 1
fun CSProperty<Int>.previous() = value - 1