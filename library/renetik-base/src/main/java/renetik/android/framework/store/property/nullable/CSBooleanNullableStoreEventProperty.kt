package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStore

class CSBooleanNullableStoreEventProperty(
    store: CSStore, key: String, value: Boolean? = null,
    onChange: ((value: Boolean?) -> Unit)? = null)
    : CSNullableStoreEventProperty<Boolean>(store,
    key, value, listenStoreChanged = false, onChange) {
    override fun get(store: CSStore) = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}

