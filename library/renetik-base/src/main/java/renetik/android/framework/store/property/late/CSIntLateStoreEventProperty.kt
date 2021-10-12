package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

class CSIntLateStoreEventProperty(
    store: CSStoreInterface, key: String, onChange: ((value: Int) -> Unit)? = null)
    : CSLateStoreEventProperty<Int>(store, key, onChange), CSStoreEventProperty<Int> {
    override fun get() = store.getInt(key)
    override fun set(store: CSStoreInterface, value: Int) = store.set(key, value)
}

