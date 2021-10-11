package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSBooleanValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)?)
    : CSValueStoreEventProperty<Boolean>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface) = store.getBoolean(key)
    override fun save(store: CSStoreInterface, value: Boolean) = store.save(key, value)
}