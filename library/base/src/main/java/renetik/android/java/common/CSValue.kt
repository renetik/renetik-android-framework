package renetik.android.java.common

interface CSValueInterface<T> {
    val value: T
}

class CSValue<T>(override var value: T) : CSValueInterface<T>
