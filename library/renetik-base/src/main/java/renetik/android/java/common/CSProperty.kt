package renetik.android.java.common

interface CSProperty<T> {
    var value: T
}

fun <T> CSProperty(value: T): CSProperty<T> = CSPropertyImplementation(value)

private data class CSPropertyImplementation<T>(override var value: T) : CSProperty<T>
