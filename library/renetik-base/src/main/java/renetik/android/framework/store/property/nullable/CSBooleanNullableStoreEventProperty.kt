package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface

class CSBooleanNullableStoreEventProperty(
    store: CSStoreInterface, key: String, value: Boolean? = null,
    onChange: ((value: Boolean?) -> Unit)? = null)
    : CSNullableStoreEventProperty<Boolean>(store, key, value, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface) = store.getBoolean(key)
    override fun save(store: CSStoreInterface, value: Boolean?) = store.save(key, value)
}

