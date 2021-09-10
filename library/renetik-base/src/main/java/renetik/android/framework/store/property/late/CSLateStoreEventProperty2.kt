package renetik.android.framework.store.property.late

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty
import renetik.kotlin.CSUnexpectedException
import renetik.kotlin.CSUnexpectedException.Companion.unexpected

abstract class CSLateStoreEventProperty2<T>(
    override val store: CSStoreInterface,
    override val key: String, onChange: ((value: T) -> Unit)?)
    : CSEventPropertyBase<T>(onChange), CSStoreEventProperty<T> {

    var _value: T? = null
        set(value) {
            field = value
            save(value!!)
        }

    override var value: T
        get() {
            if (_value == null) _value = load() ?: throw unexpected
            return _value!!
        }
        set(value) {
            value(value)
        }

    abstract fun load(): T?
    abstract fun save(value: T)

    override fun value(newValue: T, fire: Boolean) = apply {
        if (_value == newValue) return this
        if (store.has(key) && fire) eventBeforeChange.fire(value)
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}