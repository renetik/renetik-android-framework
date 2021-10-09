package renetik.android.framework.store.property.preset

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.save

abstract class CSValueStoreEventProperty<T>(
    override var store: CSStoreInterface,
    final override val key: String,
    private val getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onChange), CSPresetStoreEventProperty<T> {

    constructor(store: CSStoreInterface, key: String, default: T,
                onChange: ((value: T) -> Unit)? = null) :
            this(store, key, getDefault = { default }, onChange)

    protected abstract var _value: T

    override var value: T
        get() = _value
        set(value) {
            value(value)
        }

    protected fun firstLoad() = if (store.has(key)) load() else getDefault().also { save(it) }

    abstract fun loadNullable(store: CSStoreInterface): T?

    override fun load(store: CSStoreInterface) = loadNullable(store) ?: getDefault()

    override fun reload() {
        if (!store.has(key)) value(getDefault())
        else {
            val newValue = load(store)
            if (_value == newValue) return
            val before = _value
            _value = newValue
            onApply?.invoke(newValue)
            fireChange(before, newValue)
        }
    }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        val before = _value
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }

    override fun toString() = "$key $value"

    fun apply() = apply {
        val before = value
        val value = this.value
        onApply?.invoke(value)
        fireChange(before, value)
    }
}