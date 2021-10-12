package renetik.android.framework.store.property.nullable

import renetik.android.framework.event.property.CSEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

abstract class CSNullableStoreEventProperty<T>(
    override var store: CSStoreInterface,
    override val key: String,
    val defaultValue: T?,
    onApply: ((value: T?) -> Unit)? = null)
    : CSEventPropertyBase<T?>(onApply), CSStoreEventProperty<T?> {

    abstract fun get(store: CSStoreInterface): T?

    var isLoaded = false

    protected var _value: T? = null

    override var value: T?
        get() {
            if (!isLoaded) {
                _value = load()
                isLoaded = true
            }
            return _value
        }
        set(value) = value(value)

    fun load() = get(store) ?: defaultValue?.also { set(store, it) }

    override fun value(newValue: T?, fire: Boolean) {
        if (value == newValue) return
        val before = value
        _value = newValue
        set(store, value)
        onApply?.invoke(newValue)
        if (fire) fireChange(before, newValue)
    }
}