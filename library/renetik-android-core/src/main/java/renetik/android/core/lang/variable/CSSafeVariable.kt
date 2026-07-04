package renetik.android.core.lang.variable

import renetik.android.core.lang.value.CSSafeValue

interface CSSafeVariable<T> : CSSafeValue<T>, CSVariable<T> {
    override fun getAndSet(newValue: T): T
    fun compareAndSet(value: T, newValue: T): Boolean
}