package renetik.android.store.property.value

import renetik.android.store.CSStore

open class CSBooleanValueStoreEventProperty(
    store: CSStore, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSValueStoreEventProperty<Boolean>(store, key, listenStoreChanged = false, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean) = store.set(key, value)
}