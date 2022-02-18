package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSLongValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Long,
    onChange: ((value: Long) -> Unit)?)
    : CSValueStoreEventProperty<Long>(store, key, listenStoreChanged = false, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getLong(key)
    override fun set(store: CSStoreInterface, value: Long) = store.set(key, value)
}