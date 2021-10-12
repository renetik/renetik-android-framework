package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

class CSBooleanLateStoreEventProperty(
    store: CSStoreInterface, key: String, onChange: ((value: Boolean) -> Unit)? = null)
    : CSLateStoreEventProperty<Boolean>(store, key, onChange), CSStoreEventProperty<Boolean> {
    override fun get() = store.getBoolean(key)
    override fun set(store: CSStoreInterface, value: Boolean) = store.set(key, value)
}