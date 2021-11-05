package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface

class CSIntNullableStoreEventProperty(
    store: CSStoreInterface, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null)
    : CSNullableStoreEventProperty<Int>(store,
    key, default, listenStoreChanged = false, onChange) {
    override fun get(store: CSStoreInterface): Int? = store.getInt(key)
    override fun set(store: CSStoreInterface, value: Int?) {
        if (value == null) store.clear(key) else store.set(key, value)
    }
}