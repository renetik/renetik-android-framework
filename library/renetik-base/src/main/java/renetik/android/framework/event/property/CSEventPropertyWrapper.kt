package renetik.android.framework.event.property

abstract class CSEventPropertyWrapper<T> :
    CSEventProperty<T> {
    protected abstract val property: CSEventProperty<T>
    override fun value(newValue: T, fire: Boolean) = property.value(newValue, fire)
    override fun onChange(function: (T) -> Unit) = property.onChange(function)
    override var value: T
        get() = property.value
        set(value) = property.value(value)
}