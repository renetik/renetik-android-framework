package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.value.CSValueStoreEventProperty

class CSIntValueStoreEventProperty(
    store: CSStoreInterface, key: String, default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSValueStoreEventProperty<Int>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface): Int? = store.getInt(key)
    override fun save(store: CSStoreInterface, value: Int) = store.save(key, value)
}

