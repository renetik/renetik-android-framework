package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreDoubleEventProperty(
    store: CSStoreInterface, key: String, val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSStoreEventPropertyBase<Double>(store, key, onChange) {
    override var _value = load(store)
    override fun load(store: CSStoreInterface) = store.getDouble(key, default)
    override fun save(store: CSStoreInterface, value: Double) = store.save(key, value)
}