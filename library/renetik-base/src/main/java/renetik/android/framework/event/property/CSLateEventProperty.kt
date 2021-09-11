package renetik.android.framework.event.property

class CSLateEventProperty<T>(onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {

    private var _value: T? = null

    override fun value(newValue: T, fire: Boolean) = apply {
        if (_value == newValue) return this
        if (fire && _value != null) eventBeforeChange.fire(_value!!)
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override var value: T
        get() = _value!!
        set(value) {
            value(value)
        }
}