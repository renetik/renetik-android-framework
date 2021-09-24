package renetik.android.framework.store.property.nullable

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.preset.CSPresetStoreEventProperty
import renetik.android.framework.store.property.save

abstract class CSNullableStoreEventProperty<T>(
    override var store: CSStoreInterface,
    override val key: String,
    private val initialValue: T?,
    onApply: ((value: T?) -> Unit)? = null)
    : CSEventPropertyBase<T?>(onApply), CSPresetStoreEventProperty<T?> {

    protected abstract var _value: T?

    override var value: T?
        get() = _value
        set(value) {
            value(value)
        }

    protected fun firstLoad() = if (store.has(key)) load() else run {
        initialValue?.let { save(it) }
        initialValue
    }

    override fun reload() {
        if (!store.has(key)) initialValue?.let { value(it) }
        else {
            val newValue = load(store)
            if (_value == newValue) return
            val before = _value
            _value = newValue
            onApply?.invoke(newValue)
            fireChange(before, newValue)
        }
    }

    override fun value(newValue: T?, fire: Boolean) = apply {
        if (_value == newValue) return this
        val before = _value
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }
}