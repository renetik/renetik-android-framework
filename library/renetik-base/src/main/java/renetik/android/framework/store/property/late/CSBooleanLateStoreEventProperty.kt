package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStore
import renetik.android.framework.store.property.CSStoreEventProperty

class CSBooleanLateStoreEventProperty(
    store: CSStore, key: String, onChange: ((value: Boolean) -> Unit)? = null)
    : CSLateStoreEventProperty<Boolean>(store, key, onChange), CSStoreEventProperty<Boolean> {
    override fun get() = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean) = store.set(key, value)
}