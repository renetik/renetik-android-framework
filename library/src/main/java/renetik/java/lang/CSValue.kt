package renetik.java.lang

interface CSIValueInterface<T> {
    val value: T
}

class CSValue<T>(override var value: T) : CSIValueInterface<T>
