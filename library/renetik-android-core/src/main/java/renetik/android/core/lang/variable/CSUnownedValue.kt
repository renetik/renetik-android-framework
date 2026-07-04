package renetik.android.core.lang.variable

import renetik.android.core.lang.value.CSValue
import java.lang.ref.WeakReference

class CSUnownedValue<T>(value: T) : CSValue<T> {
    companion object {
        fun <T> unownedVal(value: T) = CSUnownedValue(value)
    }

    private var reference = WeakReference(value)

    override val value: T get() = reference.get()!!
}