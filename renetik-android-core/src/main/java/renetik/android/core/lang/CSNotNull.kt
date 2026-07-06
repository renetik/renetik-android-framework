package renetik.android.core.lang

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private object UNINITIALIZED

fun <T> notNull(): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
    private var _value: Any? = UNINITIALIZED

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (_value === UNINITIALIZED) {
            throw IllegalStateException("Property ${property.name} should be initialized before get.")
        }
        @Suppress("UNCHECKED_CAST")
        return _value as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _value = value
    }
}