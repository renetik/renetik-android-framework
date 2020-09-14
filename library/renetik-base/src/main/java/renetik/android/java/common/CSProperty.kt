package renetik.android.java.common

interface CSValue<T> {
    val value: T
}

//fun <T> value(value: T): CSValue<T> = CSValueImplementation(value)
//private data class CSValueImplementation<T>(override val value: T) : CSValue<T>

interface CSProperty<T> : CSValue<T> {
    override var value: T
}

//fun <T> property(value: T): CSProperty<T> = CSPropertyImplementation(value)
//private data class CSPropertyImplementation<T>(override var value: T) : CSProperty<T>

fun <T> CSProperty<T>.value(value: T) = apply { this.value = value }

fun CSProperty<Boolean>.setTrue() = apply { this.value = true }
fun CSProperty<Boolean>.setFalse() = apply { this.value = false }
