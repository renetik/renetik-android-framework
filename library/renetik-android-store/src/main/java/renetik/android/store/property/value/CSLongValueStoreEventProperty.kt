package renetik.android.store.property.value

import renetik.android.store.CSStore

class CSLongValueStoreEventProperty(
    store: CSStore, key: String, default: Long,
    onChange: ((value: Long) -> Unit)?)
    : CSValueStoreEventProperty<Long>(store, key, listenStoreChanged = false, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getLong(key)
    override fun set(store: CSStore, value: Long) = store.set(key, value)
}