package renetik.android.store.property.nullable

import renetik.android.store.CSStore
import renetik.android.store.property.value.CSValueStoreProperty

class CSBooleanNullableStoreProperty(
    store: CSStore, key: String,
    override val default: Boolean? = null,
    onChange: ((value: Boolean?) -> Unit)? = null)
    : CSValueStoreProperty<Boolean?>(store, key, onChange) {
    override fun get(store: CSStore) = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean?) =
        value?.let { store.set(key, value) } ?: store.clear(key)
}

