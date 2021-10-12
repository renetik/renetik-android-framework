package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface

class CSFloatNullableStoreEventProperty(
    store: CSStoreInterface, key: String, default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullableStoreEventProperty<Float>(store, key, default, onChange) {
    override fun get(store: CSStoreInterface): Float? = store.getFloat(key)
    override fun set(store: CSStoreInterface, value: Float?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}