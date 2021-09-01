package renetik.android.framework.store.property.late

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

abstract class CSLateStoreEventProperty<T>(
    override val store: CSStoreInterface,
    override val key: String, onChange: ((value: T) -> Unit)?)
    : CSEventPropertyBase<T>(onChange), CSStoreEventProperty<T> {
    override var _value: T
        get() = load()
        set(value) = save(value)

    override fun value(newValue: T, fire: Boolean) = apply {
        if (store.has(key) && value == newValue) return this
        if (store.has(key) && fire) eventBeforeChange.fire(value)
        _value = newValue
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}