package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

class CSIntLateStoreEventProperty(
    store: CSStoreInterface, key: String, onChange: ((value: Int) -> Unit)? = null)
    : CSLateStoreEventProperty<Int>(store, key, onChange), CSStoreEventProperty<Int> {
    override fun load() = store.getInt(key)
    override fun save(store: CSStoreInterface, value: Int) = store.save(key, value)
}

