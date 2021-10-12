package renetik.android.framework.store.property.late

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSValuesItemLateStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: Iterable<T>, onChange: ((value: T) -> Unit)? = null
) : CSLateStoreEventProperty<T>(store, key, onChange) {
    constructor(store: CSStoreInterface, key: String,
                values: Array<T>, onChange: ((value: T) -> Unit)? = null)
            : this(store, key, values.asIterable(), onChange)

    override fun get(): T? = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value.toId())
}