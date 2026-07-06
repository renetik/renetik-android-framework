package renetik.android.core.lang.variable

import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.value.CSValue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface CSVariable<T> : CSValue<T>, ReadWriteProperty<Any?, T> {

    fun getAndSet(newValue: T): T = value.also { this.value = newValue }

    companion object {
        fun <T> variable(value: T, onChange: ArgFun<T>? = null): CSVariable<T> =
            CSVariableImpl(value, onChange)

        fun <T> variableNull(value: T? = null, onChange: ArgFun<T?>? = null): CSVariable<T?> =
            variable(value, onChange)

        fun <T> variable(onChange: ArgFun<T>? = null): CSVariable<T> = CSLateVariableImpl(onChange)

        fun <T> variable(from: () -> T, to: (T) -> Unit): CSVariable<T> =
            CSVariableComputed(from, to)

        @JvmName("variableComputed1") fun <T, V> CSVariable<V>.variable(from: (V) -> T,
            to: (V, T) -> Unit): CSVariable<T> =
            CSVariableComputed(from = { from(value) }, to = { to(value, it) })

        @JvmName("variableComputed2")
        fun <T, V, Variable : CSVariable<V>> Variable.variable(get: (Variable).() -> T,
            set: (Variable).(T) -> Unit): CSVariable<T> =
            CSVariableComputed(from = { get(this) }, to = { set(this, it) })
    }

    override var value: T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        synchronized(this) { value }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        synchronized(this) { this.value = value }
}