package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSFloatValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSValueStoreEventProperty<Float>(store, key, listenStoreChanged = false,onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getFloat(key)
    override fun set(store: CSStoreInterface, value: Float) = store.set(key, value)
}