package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSBooleanValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSValueStoreEventProperty<Boolean>(store, key, listenStoreChanged = false, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getBoolean(key)
    override fun set(store: CSStoreInterface, value: Boolean) = store.set(key, value)
}