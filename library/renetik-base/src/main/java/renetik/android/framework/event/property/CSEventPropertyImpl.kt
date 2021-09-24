package renetik.android.framework.event.property

class CSEventPropertyImpl<T>(
    value: T, onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {

    private var _value: T = value

    override fun value(newValue: T, fire: Boolean)  {
        if (_value == newValue) return
        val before = _value
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }

    fun apply() = apply {
        val before = value
        val value = this.value
        onApply?.invoke(value)
        fireChange(before, value)
    }
}

