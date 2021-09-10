package renetik.android.framework.store.property.preset

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemValueStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T>, default: T, onChange: ((value: T) -> Unit)? = null
) : CSValueStoreEventProperty<T>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getValue(key, values)
    override fun save(store: CSStoreInterface, value: T) = store.save(key, value.toId())
}