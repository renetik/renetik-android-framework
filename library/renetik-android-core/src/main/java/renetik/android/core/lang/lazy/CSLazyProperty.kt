package renetik.android.core.lang.lazy

interface CSLazyProperty<T> : Lazy<T?> {
    override val value: T?
    fun reset()
}