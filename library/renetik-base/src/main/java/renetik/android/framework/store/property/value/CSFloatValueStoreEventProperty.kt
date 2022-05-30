package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStore

class CSFloatValueStoreEventProperty(
    store: CSStore, key: String, default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSValueStoreEventProperty<Float>(store, key, listenStoreChanged = false,onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getFloat(key)
    override fun set(store: CSStore, value: Float) = store.set(key, value)
}

