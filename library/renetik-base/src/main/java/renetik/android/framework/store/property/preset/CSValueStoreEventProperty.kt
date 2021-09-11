package renetik.android.framework.store.property.preset

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface

abstract class CSValueStoreEventProperty<T>(
    override var store: CSStoreInterface,
    override val key: String,
    private val default: T,
    onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply), CSPresetStoreEventProperty<T> {

    protected abstract var _value: T

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }

    protected fun firstLoad() = if (store.has(key)) load() else run {
        save(default)
        default
    }

    abstract fun loadNullable(store: CSStoreInterface): T?

    override fun load(store: CSStoreInterface) = loadNullable(store) ?: default

    override fun reload() {
        if (!store.has(key)) value(default)
        else {
            val newValue = load(store)
            if (_value == newValue) return
            eventBeforeChange.fire(_value)
            _value = newValue
            onApply?.invoke(newValue)
            eventChange.fire(newValue)
        }
    }

    override fun value(newValue: T, fire: Boolean) = apply {
        if (_value == newValue) return this
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    override fun toString() = value.toString()
}