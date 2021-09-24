package renetik.android.framework.event.property

class CSLateEventProperty<T>(onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {

    private var _value: T? = null

    override fun value(newValue: T, fire: Boolean) = apply {
        if (_value == newValue) return this
        val before = _value
        _value = newValue
        onApply?.invoke(newValue)
        if (fire && before != null) fireChange(before, newValue)
    }

    override var value: T
        get() = _value!!
        set(value) {
            value(value)
        }
}