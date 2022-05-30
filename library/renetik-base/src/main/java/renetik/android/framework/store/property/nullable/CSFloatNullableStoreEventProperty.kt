package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStore

class CSFloatNullableStoreEventProperty(
    store: CSStore, key: String, default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullableStoreEventProperty<Float>(store,
    key, default, listenStoreChanged = false, onChange) {
    override fun get(store: CSStore): Float? = store.getFloat(key)
    override fun set(store: CSStore, value: Float?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}