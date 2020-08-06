package renetik.android.java.common

interface CSValue<T> {
    var value: T
}

fun <T> CSValue(value: T): CSValue<T> = CSValueImplementation(value)

private data class CSValueImplementation<T>(override var value: T) : CSValue<T>
