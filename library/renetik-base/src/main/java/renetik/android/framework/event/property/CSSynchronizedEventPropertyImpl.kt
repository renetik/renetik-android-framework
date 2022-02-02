package renetik.android.framework.event.property

import renetik.android.framework.lang.CSValue
import renetik.android.framework.lang.property.CSProperty

class CSSynchronizedEventPropertyImpl<T>(
    value: T, onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply), CSSynchronizedEventProperty<T> {

    @get:Synchronized
    private var _value: T = value

    override fun value(newValue: T, fire: Boolean): Unit = synchronized(this) {
        if (_value == newValue) return
        _value = newValue
        onMain {
            onApply?.invoke(newValue)
            if (fire) eventChange.fire(newValue)
        }
    }

    override var value: T
        get() = _value
        set(value) = value(value)
}

interface CSSynchronizedValue<T> : CSValue<T>
interface CSSynchronizedProperty<T> : CSSynchronizedValue<T>, CSProperty<T>
interface CSSynchronizedEventProperty<T> : CSSynchronizedProperty<T>, CSEventProperty<T>

class CSSynchronizedPropertyImpl<T>(
    value: T,
    val onChange: ((value: T) -> Unit)? = null)
    : CSSynchronizedProperty<T> {

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