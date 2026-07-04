package renetik.android.store.property.nullable

import renetik.android.store.CSStore
import renetik.android.store.property.value.CSValueStoreProperty

class CSFloatNullableStoreProperty(
    store: CSStore, key: String,
    override val default: Float? = null,
    onChange: ((value: Float?) -> Unit)? = null)
    : CSValueStoreProperty<Float?>(store, key, onChange) {
    override fun get(store: CSStore) = store.getFloat(key)
    override fun set(store: CSStore, value: Float?) =
        value?.let { store.set(key, value) } ?: store.clear(key)
}