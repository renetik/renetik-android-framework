package renetik.android.framework.event.property

class CSSynchronizedEventPropertyImpl<T>(
    value: T, onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply), CSSynchronizedEventProperty<T> {

    @get:Synchronized
    private var _value: T = value

    override fun value(newValue: T, fire: Boolean): Unit = synchronized(this) {
        if (_value == newValue) return
        _value = newValue
        onMain { onValueChanged(newValue) }
    }

    override var value: T
        get() = _value
        set(value) = value(value)
}