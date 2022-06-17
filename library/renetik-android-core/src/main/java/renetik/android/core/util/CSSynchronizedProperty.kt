package renetik.android.core.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSSynchronizedProperty<T>(value: T, private val didSet: ((T) -> Unit)? = null)
    : ReadWriteProperty<Any, T> {

    companion object {
        fun <T> synchronized(value: T, didSet: ((T) -> Unit)? = null) =
            CSSynchronizedProperty(value, didSet)
    }

    private var field = value

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        kotlin.synchronized(this) { field }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        kotlin.synchronized(this) {
            field = value
            didSet?.invoke(value)
        }
    }
}