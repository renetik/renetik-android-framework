package renetik.android.core.lang.variable

class CSVariableComputed<T>(
    val from: () -> T, val to: (T) -> Unit) : CSVariable<T> {
    override var value: T
        get() = from()
        set(value) = to(value)
}