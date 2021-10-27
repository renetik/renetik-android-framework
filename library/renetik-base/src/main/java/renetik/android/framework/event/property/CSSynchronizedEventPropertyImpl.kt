package renetik.android.framework.event.property

import renetik.android.framework.lang.CSValue
import renetik.android.framework.lang.property.CSProperty
import renetik.kotlin.later

class CSSynchronizedEventPropertyImpl<T>(
    value: T, onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply), CSSynchronizedProperty<T> {

    @get:Synchronized
    private var _value: T = value

    override fun value(newValue: T, fire: Boolean) {
        synchronized(this) {
            if (_value == newValue) return
            _value = newValue
            later { onApply?.invoke(newValue) }
            if (fire) later { eventChange.fire(newValue) }
        }
    }

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }
}

interface CSSynchronizedValue<T> : CSValue<T>
interface CSSynchronizedProperty<T> : CSSynchronizedValue<T>

class CSSynchronizedPropertyImpl<T>(
    value: T,
    val onChange: ((value: T) -> Unit)? = null)
    : CSProperty<T>, CSSynchronizedProperty<T> {

    @get:Synchronized
    @set:Synchronized
    override var value: T = value
        set(value) {
            field = value
            onChange?.invoke(value)
        }

    fun value(value: T) {
        this.value = value
    }
}