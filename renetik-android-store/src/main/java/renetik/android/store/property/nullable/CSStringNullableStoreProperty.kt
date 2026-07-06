package renetik.android.store.property.nullable

import renetik.android.store.CSStore
import renetik.android.store.property.value.CSValueStoreProperty

class CSStringNullableStoreProperty(
    store: CSStore, key: String,
    override val default: String? = null,
    onChange: ((value: String?) -> Unit)? = null)
    : CSValueStoreProperty<String?>(store, key, onChange) {
    override fun get(store: CSStore): String? = store.getString(key)
    override fun set(store: CSStore, value: String?) =
        value?.let { store.set(key, value) } ?: store.clear(key)
}

