package renetik.android.store.property.late

import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreEventProperty

class CSStringLateStoreEventProperty(
    store: CSStore, key: String, onChange: ((value: String) -> Unit)?)
    : CSLateStoreEventProperty<String>(store, key, onChange), CSStoreEventProperty<String> {
    override fun get() = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

