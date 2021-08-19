package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyBase

abstract class CSStoreEventPropertyBase2<T>(
    var store: CSStoreInterface,
    val key: String,
    val default: T,
    onApply: ((value: T) -> Unit)? = null)
    : CSEventPropertyBase<T>(onApply), CSStoreEventProperty<T> {

    abstract fun loadNullable(store: CSStoreInterface): T?

    override fun load(store: CSStoreInterface) = loadNullable(store) ?: default

    override val isSet get() = store.has(key)

    override fun reload() {
        val newValue = loadNullable(store)
        if (newValue == null) {
            save(store, default)
            value(default)
            return
        }
        if (_value == newValue) return
        eventBeforeChange.fire(_value)
        _value = newValue
        onApply?.invoke(newValue)
        eventChange.fire(newValue)
    }

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        if (fire) eventBeforeChange.fire(_value)
        _value = newValue
        save(store, value)
        onApply?.invoke(newValue)
        if (fire) eventChange.fire(newValue)
    }

    protected fun firstLoad() = loadNullable(store) ?: run {
        save(store, default)
        default
    }
}