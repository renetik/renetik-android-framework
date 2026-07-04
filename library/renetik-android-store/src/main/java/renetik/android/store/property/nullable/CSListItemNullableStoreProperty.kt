package renetik.android.store.property.nullable

import renetik.android.core.kotlin.toId
import renetik.android.json.obj.getValue
import renetik.android.store.CSStore
import renetik.android.store.property.value.CSValueStoreProperty

class CSListItemNullableStoreProperty<T>(
    store: CSStore, key: String,
    val getValues: () -> List<T>,
    val getDefault: () -> T?,
    onChange: ((value: T?) -> Unit)? = null)
    : CSValueStoreProperty<T?>(store, key, onChange) {
    val values: Iterable<T> get() = getValues()
    override val default: T? get() = getDefault()
    override fun get(store: CSStore) = store.getValue(key, values)
    override fun set(store: CSStore, value: T?) =
        value?.let { store.set(key, value.toId()) } ?: store.clear(key)
}