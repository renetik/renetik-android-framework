package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSIntValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Int,
    onChange: ((value: Int) -> Unit)? = null)
    : CSValueStoreEventProperty<Int>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface) = store.getInt(key)
    override fun save(store: CSStoreInterface,value: Int) = store.save(key, value)
}

