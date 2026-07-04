package renetik.android.core.lang.atomic

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSNullableAtomic<T>(@Volatile private var value: T? = null) : ReadWriteProperty<Any?, T?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
    }

    companion object {
        fun <T> nullableAtomic(value: T? = null) = CSNullableAtomic(value)
    }
}