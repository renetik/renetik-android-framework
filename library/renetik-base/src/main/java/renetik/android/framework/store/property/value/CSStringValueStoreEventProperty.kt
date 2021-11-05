package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSStringValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: String,
    onChange: ((value: String) -> Unit)? = null)
    : CSValueStoreEventProperty<String>(store, key, listenStoreChanged = false, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getString(key)
    override fun set(store: CSStoreInterface, value: String) = store.set(key, value)
}

