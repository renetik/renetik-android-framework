package renetik.android.store.property.value

import renetik.android.store.CSStore

class CSStringValueStoreEventProperty(
    store: CSStore, key: String, default: String,
    listenStoreChanged: Boolean = false,
    onChange: ((value: String) -> Unit)? = null)
    : CSValueStoreEventProperty<String>(store, key, listenStoreChanged, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

