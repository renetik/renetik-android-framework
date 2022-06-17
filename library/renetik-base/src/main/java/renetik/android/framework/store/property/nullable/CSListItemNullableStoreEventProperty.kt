package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStore
import renetik.android.framework.store.getValue
import renetik.android.core.kotlin.toId

class CSListItemNullableStoreEventProperty<T>(
    store: CSStore, key: String,
    val values: List<T>, default: T? = null, onChange: ((value: T?) -> Unit)? = null
) : CSNullableStoreEventProperty<T>(store,
    key, default, listenStoreChanged = false, onChange) {
    override fun get(store: CSStore) = store.getValue(key, values)
    override fun set(store: CSStore, value: T?) {
        if (value == null) store.clear(key) else store.set(key, value.toId())
    }
}