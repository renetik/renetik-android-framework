package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.android.java.extensions.toId

class CSItemNullableStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T?>, default: T?, onChange: ((value: T?) -> Unit)? = null
) : CSNullableStoreEventProperty<T?>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun load(store: CSStoreInterface) = store.getValue(key, values)
    override fun save(store: CSStoreInterface, value: T?) {
        if (value == null) store.clear(key) else store.save(key, value.toId())
    }
}