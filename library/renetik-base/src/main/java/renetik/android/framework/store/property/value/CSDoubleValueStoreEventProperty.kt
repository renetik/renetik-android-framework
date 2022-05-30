package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStore

class CSDoubleValueStoreEventProperty(
    store: CSStore, key: String, default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValueStoreEventProperty<Double>(store, key, listenStoreChanged = false,onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getDouble(key)
    override fun set(store: CSStore, value: Double) = store.set(key, value)
}