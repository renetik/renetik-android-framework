package renetik.android.event.property

import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.variable.CSSafeVariable
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

class CSSafeVariableImpl<T>(
    value: T, private val onChangeUnsafe: ArgFun<T>? = null
) : CSSafeVariable<T> {
    private val field = AtomicReference(value)
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = field.get()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = value(value)
    override fun getAndSet(newValue: T): T {
        val previous = field.getAndSet(newValue)
        if (previous != newValue) onChangeUnsafe?.invoke(newValue)
        return previous
    }

    override fun compareAndSet(value: T, newValue: T): Boolean {
        val isSet = field.compareAndSet(value, newValue)
        if (isSet) onChangeUnsafe?.invoke(newValue)
        return isSet
    }

    override var value: T
        get() = this.field.get()
        set(value) = value(value)

    private fun value(newValue: T) {
        if (field.getAndSet(newValue) != newValue)
            onChangeUnsafe?.invoke(newValue)
    }

    companion object {
        fun <T> safeVar(value: T, onChangeUnsafe: ArgFun<T>? = null) =
            CSSafeVariableImpl(value, onChangeUnsafe)
    }
}