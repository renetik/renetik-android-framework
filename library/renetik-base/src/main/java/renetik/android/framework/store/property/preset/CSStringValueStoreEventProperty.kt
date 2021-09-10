package renetik.android.framework.store.property.preset

import renetik.android.framework.store.CSStoreInterface

class CSStringValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: String,
    onChange: ((value: String) -> Unit)?)
    : CSValueStoreEventProperty<String>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getString(key)
    override fun save(store: CSStoreInterface, value: String) = store.save(key, value)
}

