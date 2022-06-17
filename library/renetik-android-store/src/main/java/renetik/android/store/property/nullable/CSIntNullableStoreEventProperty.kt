package renetik.android.store.property.nullable

import renetik.android.store.CSStore

class CSIntNullableStoreEventProperty(
    store: CSStore, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null)
    : CSNullableStoreEventProperty<Int>(store,
    key, default, listenStoreChanged = false, onChange) {
    override fun get(store: CSStore): Int? = store.getInt(key)
    override fun set(store: CSStore, value: Int?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}