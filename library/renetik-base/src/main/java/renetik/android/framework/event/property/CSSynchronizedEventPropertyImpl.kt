package renetik.android.framework.event.property

import renetik.kotlin.later

class CSSynchronizedEventPropertyImpl<T>(
    value: T, onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply) {

    @get:Synchronized
    private var _value: T = value

    override fun value(newValue: T, fire: Boolean) = apply {
        synchronized(this) {
            if (_value == newValue) return this
            if (fire) later { eventBeforeChange.fire(_value) }
            _value = newValue
            onApply?.invoke(newValue)
            if (fire) later { eventChange.fire(newValue) }
        }
    }

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }
}