package renetik.android.store.property.value

import renetik.android.store.CSStore

class CSIntListValueStoreProperty(
    store: CSStore, key: String,
    override val default: List<Int>,
    onChange: ((value: List<Int>) -> Unit)? = null
) : CSValueStoreProperty<List<Int>>(store, key, onChange) {

    override fun get(store: CSStore) = store.getString(key)?.split(",")
        ?.map { it.toInt() } ?: default

    override fun set(store: CSStore, value: List<Int>) =
        store.set(key, value.joinToString(","))
}