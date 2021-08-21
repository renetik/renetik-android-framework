package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSValueStoreEventProperty

class CSFloatValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Float,
    onChange: ((value: Float) -> Unit)?)
    : CSValueStoreEventProperty<Float>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getFloat(key)
    override fun save(store: CSStoreInterface, value: Float) = store.save(key, value)
}