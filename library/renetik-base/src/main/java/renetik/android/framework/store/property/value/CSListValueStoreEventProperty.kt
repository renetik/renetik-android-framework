package renetik.android.framework.store.property.value

import renetik.android.framework.lang.CSHasId
import renetik.android.framework.store.CSStore
import renetik.kotlin.toId

class CSListValueStoreEventProperty<T : CSHasId>(
    store: CSStore, key: String,
    val values: Iterable<T>, val default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) : CSValueStoreEventProperty<List<T>>(store, key, listenStoreChanged = false, onChange) {

    override val defaultValue = default
    override var _value = load()

    override fun get(store: CSStore) = store.get(key)?.split(",")
        ?.mapNotNull { categoryId -> values.find { it.id == categoryId } } ?: default

    override fun set(store: CSStore, value: List<T>) =
        store.set(key, value.joinToString(",") { it.toId() })
}