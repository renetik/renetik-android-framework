package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreIntEventProperty(
    store: CSStoreInterface, val key: String, val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSStoreEventPropertyBase<Int>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getInt(key, default)
    override fun setValue(store: CSStoreInterface, value: Int) = store.save(key, value)
}