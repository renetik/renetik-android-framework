package renetik.android.core.lang.variable

import renetik.android.core.lang.value.CSValue
import java.lang.ref.WeakReference

class CSWeakValue<T>(value: T) : CSValue<T?> {
    companion object {
        fun <T> weakVal(value: T) = CSWeakValue(value)
    }

    private var reference = WeakReference(value)

    override val value: T? get() = reference.get()
}