package renetik.android.framework.store.property.nullable

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSPresetStoreEventProperty

abstract class CSNullableStoreEventProperty<T>(
    override var store: CSStoreInterface,
    override val key: String,
    private val initialValue: T?,
    onApply: ((value: T?) -> Unit)? = null)
    : CSEventPropertyBase<T?>(onApply), CSPresetStoreEventProperty<T?> {

    protected fun firstLoad() = if (store.has(key)) load() else run {
        initialValue?.let { save(it) }
        initialValue
    }

    override fun reload() {
        if (!store.has(key)) initialValue?.let { value(it) }
        else {
            val newValue = load(store)
            if (_value == newValue) return
            eventBeforeChange.fire(_value)
            _value = newValue
            onApply?.invoke(newValue)
            eventChange.fire(newValue)
        }
    }

    override fun value(newValue: T?, fire: Boolean) {
        if (_value == newValue) return
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }
}