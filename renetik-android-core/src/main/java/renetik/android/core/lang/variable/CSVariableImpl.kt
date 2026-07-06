package renetik.android.core.lang.variable

import renetik.android.core.lang.ArgFun
import kotlin.reflect.KProperty

class CSVariableImpl<T>(
    override var value: T,
    val onChange: ArgFun<T>? = null
) : CSVariable<T> {

    fun apply() = this.also { onChange?.invoke(value) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        synchronized(this) { value }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        synchronized(this) {
            if (this.value != value) {
                this.value = value
                onChange?.invoke(value)
            }
        }
}
