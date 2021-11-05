package renetik.android.framework.store.property.value

import renetik.android.framework.lang.property.CSListValuesProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.kotlin.toId

class CSListItemValueStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val getValues: () -> List<T>,
    val getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) : CSValueStoreEventProperty<T>(store, key, listenStoreChanged = false,onChange), CSListValuesProperty<T> {

    constructor(
        store: CSStoreInterface, key: String,
        values: List<T>, default: T, onChange: ((value: T) -> Unit)? = null
    ) : this(store, key, { values }, getDefault = { default }, onChange)

    override val values: List<T> get() = getValues()
    override val defaultValue: T get() = getDefault()
    override var _value: T = load()
    override fun get(store: CSStoreInterface): T? = store.getValue(key, values)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value.toId())
}