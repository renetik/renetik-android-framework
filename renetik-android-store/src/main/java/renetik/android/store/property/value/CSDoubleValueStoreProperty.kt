package renetik.android.store.property.value

import renetik.android.store.CSStore

class CSDoubleValueStoreProperty(
    store: CSStore, key: String,
    override val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValueStoreProperty<Double>(store, key, onChange) {
    override fun get(store: CSStore) = store.getDouble(key)
    override fun set(store: CSStore, value: Double) = store.set(key, value)
}