package renetik.android.framework.store.property.late

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty
import renetik.kotlin.CSUnexpectedException.Companion.unexpected

abstract class CSLateStoreEventProperty<T>(
    override val store: CSStoreInterface,
    override val key: String, onChange: ((value: T) -> Unit)?)
    : CSEventPropertyBase<T>(onChange), CSStoreEventProperty<T> {

    var _value: T? = null

    override var value: T
        get() {
            if (_value == null) _value = get()
            if (_value == null)
                throw unexpected
            return _value!!
        }
        set(value) = value(value)

    abstract fun get(): T?

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        val before = if (store.has(key)) _value else null
        _value = newValue
        set(store, newValue)
        onApply?.invoke(newValue)
        if (fire && before != null) eventChange.fire(newValue)
    }

    val isNotLoaded get() = _value == null

    val isLoaded get() = _value != null

    override fun toString() = "$key $value"
}