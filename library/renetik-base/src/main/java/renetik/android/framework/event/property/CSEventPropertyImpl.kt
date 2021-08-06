package renetik.android.framework.event.property

import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.store.CSStoreInterface

open class CSEventPropertyImpl<T>(value: T, onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {
    override var _value: T = value
}

abstract class CSStoreEventPropertyBase<T>(
    var store: CSStoreInterface, onApply: ((value: T) -> Unit)? = null) :
    CSEventPropertyBase<T>(onApply) {

    override fun onValueChanged(newValue: T) = setValue(store, newValue)

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(getValue(store))

    abstract fun getValue(store: CSStoreInterface): T
    abstract fun setValue(store: CSStoreInterface, value: T)
}

abstract class CSEventPropertyBase<T>(protected val onApply: ((value: T) -> Unit)? = null) :
    CSEventProperty<T> {

    protected val eventBeforeChange = event<T>()
    protected val eventChange = event<T>()
    protected abstract var _value: T

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        onValueChanged(newValue)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    open fun onValueChanged(newValue: T) {}

    override fun onBeforeChange(value: (T) -> Unit) = eventBeforeChange.listen(value)
    override fun onChange(value: (T) -> Unit) = eventChange.listen(value)
    override fun apply() = apply {
        onApply?.invoke(value)
        eventChange.fire(value)
    }

    override fun toString() = value.toString()
}