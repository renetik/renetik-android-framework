package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSStoreFloatEventProperty(
    store: CSStoreInterface, val key: String, val default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSStoreEventPropertyBase<Float>(store, onChange) {
    override var _value = load(store)
    override fun load(store: CSStoreInterface) = store.getFloat(key, default)
    override fun save(store: CSStoreInterface, value: Float) = store.save(key, value)
}