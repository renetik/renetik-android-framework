package renetik.android.framework.store.property.value

import renetik.android.framework.lang.CSId
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.toId

 class CSListValueStoreEventProperty<T : CSId>(
    store: CSStoreInterface, key: String,
    val values: Iterable<T>, val default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) : CSValueStoreEventProperty<List<T>>(store, key, onChange) {

    override val defaultValue = default
    override var _value = load()

    override fun get(store: CSStoreInterface) = store.get(key)?.split(",")
        ?.mapNotNull { categoryId -> values.find { it.id == categoryId } } ?: default

    override fun set(store: CSStoreInterface, value: List<T>) =
        store.set(key, value.joinToString(",") { it.toId() })
}