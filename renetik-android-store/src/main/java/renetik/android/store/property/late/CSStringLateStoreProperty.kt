package renetik.android.store.property.late

import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

class CSStringLateStoreProperty(
    store: CSStore, key: String, onChange: ((value: String) -> Unit)?)
    : CSLateStorePropertyBase<String>(store, key, onChange), CSStoreProperty<String> {
    override fun get(store: CSStore) = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

