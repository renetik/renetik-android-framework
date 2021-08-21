package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSValueStoreEventProperty

class CSIntNullableStoreEventProperty(
    store: CSStoreInterface, key: String, default: Int?,
    onChange: ((value: Int?) -> Unit)?)
    : CSNullableStoreEventProperty<Int?>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface): Int? = store.getInt(key)
    override fun save(store: CSStoreInterface, value: Int?) = store.save(key, value)
}