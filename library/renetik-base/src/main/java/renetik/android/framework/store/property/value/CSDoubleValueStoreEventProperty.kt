package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface

class CSDoubleValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValueStoreEventProperty<Double>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface) = store.getDouble(key)
    override fun save(store: CSStoreInterface,value: Double) = store.save(key, value)
}