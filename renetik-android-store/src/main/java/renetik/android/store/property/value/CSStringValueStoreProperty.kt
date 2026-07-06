package renetik.android.store.property.value

import renetik.android.store.CSStore

class CSStringValueStoreProperty(
    store: CSStore, key: String,
    private val getDefault: () -> String,
    onChange: ((value: String) -> Unit)? = null
) : CSValueStoreProperty<String>(store, key, onChange) {
    constructor(
        store: CSStore,
        key: String,
        default: String,
        onChange: ((value: String) -> Unit)? = null
    ) : this(store, key, { default }, onChange)

    override val default: String get() = getDefault()
    override fun get(store: CSStore) = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

