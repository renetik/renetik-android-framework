package renetik.android.java.common

interface CSValue<T> {
    val value: T
}

fun <T> CSValue(value: T): CSValue<T> = CSValueImplementation(value)
private data class CSValueImplementation<T>(override val value: T) : CSValue<T>


interface CSProperty<T> : CSValue<T> {
    override var value: T
}

fun <T> CSProperty(value: T): CSProperty<T> = CSPropertyImplementation(value)
private data class CSPropertyImplementation<T>(override var value: T) : CSProperty<T>
