package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSDoubleValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValueStoreEventProperty<Double>(store, key, listenStoreChanged = false,onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getDouble(key)
    override fun set(store: CSStoreInterface, value: Double) = store.set(key, value)
}