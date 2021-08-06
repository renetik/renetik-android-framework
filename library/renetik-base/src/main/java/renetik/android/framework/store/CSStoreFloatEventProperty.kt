package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreFloatEventProperty(
    store: CSStoreInterface, val key: String, val default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSStoreEventPropertyBase<Float>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getFloat(key, default)
    override fun setValue(store: CSStoreInterface, value: Float) = store.save(key, value)
}