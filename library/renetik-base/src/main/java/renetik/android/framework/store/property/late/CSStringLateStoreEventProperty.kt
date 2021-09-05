package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.CSStoreEventProperty

class CSStringLateStoreEventProperty(
    store: CSStoreInterface, key: String, onChange: ((value: String) -> Unit)?)
    : CSLateStoreEventProperty<String>(store, key, onChange), CSStoreEventProperty<String> {
    override fun load() = store.getString(key)!!
    override fun save(value: String) = save(store, value)
    fun save(store: CSStoreInterface, value: String) = store.save(key, value)
}

