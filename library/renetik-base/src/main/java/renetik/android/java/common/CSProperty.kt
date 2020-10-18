package renetik.android.java.common

interface CSValue<T> {
    val value: T
}

interface CSProperty<T> : CSValue<T> {
    override var value: T
}

fun <T> variable(value: T): CSProperty<T> = CSVariable(value)
private class CSVariable<T>(override var value: T) : CSProperty<T>

fun <T> CSProperty<T>.value(value: T) = apply { this.value = value }
fun CSProperty<Boolean>.setTrue() = apply { this.value = true }
fun CSProperty<Boolean>.setFalse() = apply { this.value = false }


