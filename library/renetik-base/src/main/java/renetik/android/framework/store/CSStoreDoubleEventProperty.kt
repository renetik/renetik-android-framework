package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreDoubleEventProperty(
    store: CSStoreInterface, val key: String, val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSStoreEventPropertyBase<Double>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getDouble(key, default)
    override fun setValue(store: CSStoreInterface, value: Double) = store.save(key, value)
}