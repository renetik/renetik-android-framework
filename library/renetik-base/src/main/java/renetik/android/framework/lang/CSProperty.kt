package renetik.android.framework.lang

interface CSValue<T> {
    val value: T
}

interface CSProperty<T> : CSValue<T> {
    override var value: T
}

fun <T> variable(value: T): CSProperty<T> = CSVariableImplementation(value)
private class CSVariableImplementation<T>(override var value: T) : CSProperty<T>

fun <T> CSProperty<T>.value(value: T) = apply { this.value = value }
fun CSProperty<Boolean>.setTrue() = apply { this.value = true }
fun CSProperty<Boolean>.setFalse() = apply { this.value = false }

fun CSProperty<Int>.increment() = apply { value++ }
fun CSProperty<Int>.decrement() = apply { value-- }


