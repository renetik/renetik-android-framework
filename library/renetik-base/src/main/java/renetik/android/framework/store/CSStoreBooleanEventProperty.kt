package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyBase

class CSStoreBooleanEventProperty(
    private var store: CSStoreInterface, val key: String, val default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSEventPropertyBase<Boolean>(onChange) {

    override var _value: Boolean
        get() = store.getBoolean(key, default)
        set(value) = store.save(key, value)

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getBoolean(key, default))
}