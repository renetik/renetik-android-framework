package renetik.android.store.property.value

import renetik.android.store.CSStore

class CSIntValueStoreEventProperty(
    store: CSStore, key: String, default: Int,
    listenStoreChanged: Boolean = false, onChange: ((value: Int) -> Unit)? = null)
    : CSValueStoreEventProperty<Int>(store, key, listenStoreChanged, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getInt(key)
    override fun set(store: CSStore, value: Int) = store.set(key, value)
}

