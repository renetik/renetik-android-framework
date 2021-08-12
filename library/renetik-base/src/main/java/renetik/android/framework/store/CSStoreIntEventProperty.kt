package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreIntEventProperty(
    store: CSStoreInterface, key: String, val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSStoreEventPropertyBase<Int>(store, key, onChange) {
    override var _value = load(store)
    override fun load(store: CSStoreInterface) = store.getInt(key, default)
    override fun save(store: CSStoreInterface, value: Int) = store.save(key, value)
}