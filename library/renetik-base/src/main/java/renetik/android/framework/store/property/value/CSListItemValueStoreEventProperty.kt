package renetik.android.framework.store.property.value

import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemValueStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    override val values: List<T>, val getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) : CSValueStoreEventProperty<T>(store, key, onChange), CSListValuesProperty<T> {

    constructor(
        store: CSStoreInterface, key: String,
        values: List<T>, default: T, onChange: ((value: T) -> Unit)? = null
    ) : this(store, key, values, getDefault = { default }, onChange)

    override val defaultValue get() = getDefault()
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value.toId())
}