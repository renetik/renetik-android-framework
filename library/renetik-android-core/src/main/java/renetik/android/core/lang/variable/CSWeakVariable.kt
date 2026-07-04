package renetik.android.core.lang.variable

import java.lang.ref.WeakReference

class CSWeakVariable<T>(value: T? = null) : CSVariable<T?> {

    companion object {
        fun <T> weak(value: T? = null) = CSWeakVariable(value)
    }

    private var reference: WeakReference<T>? = null

    override var value: T?
        get() = reference?.get()
        set(value) {
            reference = WeakReference(value)
        }

    init {
        value?.let { this.value = it }
    }
}


