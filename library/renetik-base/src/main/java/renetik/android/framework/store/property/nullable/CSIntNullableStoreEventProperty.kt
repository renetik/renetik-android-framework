package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface

class CSIntNullableStoreEventProperty(
    store: CSStoreInterface, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null)
    : CSNullableStoreEventProperty<Int?>(store, key, default, onChange) {
    override fun load(store: CSStoreInterface): Int? = store.getInt(key)
    override fun save(store: CSStoreInterface, value: Int?) {
        if (value == null) store.clear(key) else store.save(key, value)
    }
}