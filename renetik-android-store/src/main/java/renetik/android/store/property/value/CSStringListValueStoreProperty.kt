package renetik.android.store.property.value

import renetik.android.json.obj.getStringList
import renetik.android.store.CSStore

class CSStringListValueStoreProperty(
    store: CSStore, key: String,
    override val default: List<String>,
    onChange: ((value: List<String>) -> Unit)? = null
) : CSValueStoreProperty<List<String>>(store, key, onChange) {

    override fun get(store: CSStore) =
        store.getStringList(key) ?: default

    override fun set(store: CSStore, value: List<String>) =
        store.set(key, value)
}