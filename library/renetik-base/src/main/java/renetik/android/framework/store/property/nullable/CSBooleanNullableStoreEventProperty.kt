package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface

class CSBooleanNullableStoreEventProperty(
    store: CSStoreInterface, key: String, value: Boolean? = null,
    onChange: ((value: Boolean?) -> Unit)? = null)
    : CSNullableStoreEventProperty<Boolean>(store, key, value, onChange) {
    override fun get(store: CSStoreInterface) = store.getBoolean(key)
    override fun set(store: CSStoreInterface, value: Boolean?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}

