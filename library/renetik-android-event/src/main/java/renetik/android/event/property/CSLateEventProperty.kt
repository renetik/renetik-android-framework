package renetik.android.event.property

class CSLateEventProperty<T>(onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {

    private var _value: T? = null

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        onValueChanged(newValue, fire)
    }

    override var value: T
        get() = _value!!
        set(value) = value(value)
}