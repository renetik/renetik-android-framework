package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.android.java.extensions.toId

class CSItemLateStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T>, onChange: ((value: T) -> Unit)? = null
) : CSLateStoreEventProperty<T>(store, key, onChange) {
    override fun load() = store.getValue(key, values)!!
    override fun save(value: T) = save(store, value)
    fun save(store: CSStoreInterface, value: T) = store.save(key, value.toId())
}