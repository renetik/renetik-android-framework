package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStore
import renetik.android.framework.store.getValue
import renetik.android.core.kotlin.toId

class CSValuesItemLateStoreEventProperty<T>(
    store: CSStore, key: String,
    val values: Iterable<T>, onChange: ((value: T) -> Unit)? = null
) : CSLateStoreEventProperty<T>(store, key, onChange) {
    constructor(store: CSStore, key: String,
                values: Array<T>, onChange: ((value: T) -> Unit)? = null)
            : this(store, key, values.asIterable(), onChange)

    override fun get(): T? = store.getValue(key, values)
    override fun set(store: CSStore, value: T) = store.set(key, value.toId())
}