package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemNullableStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T>, default: T? = null, onChange: ((value: T?) -> Unit)? = null
) : CSNullableStoreEventProperty<T>(store,
    key, default, listenStoreChanged = false, onChange) {
    override fun get(store: CSStoreInterface) = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T?) {
        if (value == null) store.clear(key) else store.set(key, value.toId())
    }
}