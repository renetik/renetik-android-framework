package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSIntValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Int,
    onChange: ((value: Int) -> Unit)? = null)
    : CSValueStoreEventProperty<Int>(store, key, onChange) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getInt(key)
    override fun set(store: CSStoreInterface, value: Int) = store.set(key, value)
}

