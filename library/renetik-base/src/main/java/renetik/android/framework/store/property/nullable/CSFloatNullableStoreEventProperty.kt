package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSValueStoreEventProperty

class CSFloatNullableStoreEventProperty(
    store: CSStoreInterface, key: String, default: Float?,
    onChange: ((value: Float?) -> Unit)?)
    : CSNullableStoreEventProperty<Float?>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface): Float? = store.getFloat(key)
    override fun save(store: CSStoreInterface, value: Float?) = store.save(key, value)
}