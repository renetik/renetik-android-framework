package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStore
import renetik.android.framework.store.property.CSStoreEventProperty

class CSIntLateStoreEventProperty(
    store: CSStore, key: String, onChange: ((value: Int) -> Unit)? = null)
    : CSLateStoreEventProperty<Int>(store, key, onChange), CSStoreEventProperty<Int> {
    override fun get() = store.getInt(key)
    override fun set(store: CSStore, value: Int) = store.set(key, value)
}

