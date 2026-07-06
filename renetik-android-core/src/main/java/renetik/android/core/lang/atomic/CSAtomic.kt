package renetik.android.core.lang.atomic

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSAtomic<T>(@Volatile private var value: T) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    companion object {
        fun <T> atomic(value: T) = CSAtomic(value)
    }
}