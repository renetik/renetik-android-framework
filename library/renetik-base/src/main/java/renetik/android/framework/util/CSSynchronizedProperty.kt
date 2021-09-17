package renetik.android.framework.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSSynchronizedProperty<T>(defaultValue: T) : ReadWriteProperty<Any, T> {

    companion object {
        fun <T> synchronized(defaultValue: T) = CSSynchronizedProperty(defaultValue)
    }

    private var field = defaultValue

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return synchronized(this) { field }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        synchronized(this) { field = value }
    }
}