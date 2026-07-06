package renetik.android.store.property.late

import renetik.android.core.kotlin.toId
import renetik.android.json.obj.getValue
import renetik.android.store.CSStore

class CSListItemLateStoreProperty<T>(
    store: CSStore, key: String,
    val values: Iterable<T>, onChange: ((value: T) -> Unit)? = null
) : CSLateStorePropertyBase<T>(store, key, onChange) {
    constructor(store: CSStore, key: String,
                values: Array<T>, onChange: ((value: T) -> Unit)? = null)
            : this(store, key, values.asIterable(), onChange)

    override fun get(store: CSStore): T? = store.getValue(key, values)
    override fun set(store: CSStore, value: T) = store.set(key, value.toId())
}