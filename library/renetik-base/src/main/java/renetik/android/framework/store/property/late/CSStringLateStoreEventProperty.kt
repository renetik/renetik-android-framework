package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

class CSStringLateStoreEventProperty(
    store: CSStoreInterface, key: String, onChange: ((value: String) -> Unit)?)
    : CSLateStoreEventProperty<String>(store, key, onChange), CSStoreEventProperty<String> {
    override fun get() = store.getString(key)
    override fun set(store: CSStoreInterface, value: String) = store.set(key, value)
}

