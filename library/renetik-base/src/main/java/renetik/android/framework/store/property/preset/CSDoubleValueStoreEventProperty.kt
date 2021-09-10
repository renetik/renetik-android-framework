package renetik.android.framework.store.property.preset

import renetik.android.framework.store.CSStoreInterface

class CSDoubleValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValueStoreEventProperty<Double>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface): Double? = store.getDouble(key)
    override fun save(store: CSStoreInterface, value: Double) = store.save(key, value)
}