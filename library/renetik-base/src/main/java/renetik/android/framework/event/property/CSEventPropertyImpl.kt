package renetik.android.framework.event.property

import renetik.android.framework.event.event
import renetik.android.framework.event.listen

open class CSEventPropertyImpl<T>(value: T, private val onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {
    override var _value: T = value
}

abstract class CSEventPropertyBase<T>(private val onApply: ((value: T) -> Unit)? = null) :
    CSEventProperty<T> {

    private val eventBeforeChange = event<T>()
    private val eventChange = event<T>()
    protected abstract var _value: T

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override fun onBeforeChange(value: (T) -> Unit) = eventBeforeChange.listen(value)
    override fun onChange(value: (T) -> Unit) = eventChange.listen(value)
    fun apply() = apply {
        onApply?.invoke(value)
        eventChange.fire(value)
    }

    override fun toString() = value.toString()
}