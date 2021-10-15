package renetik.android.framework.event.property

import renetik.kotlin.later

class CSSynchronizedEventPropertyImpl<T>(
    value: T, onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply) {

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