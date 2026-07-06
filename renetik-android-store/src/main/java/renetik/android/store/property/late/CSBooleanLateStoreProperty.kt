package renetik.android.store.property.late

import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

class CSBooleanLateStoreProperty(
    store: CSStore, key: String, onChange: ((value: Boolean) -> Unit)? = null)
    : CSLateStorePropertyBase<Boolean>(store, key, onChange), CSStoreProperty<Boolean> {
    override fun get(store: CSStore) = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean) = store.set(key, value)
}