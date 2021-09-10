package renetik.android.framework.store.property.preset

import renetik.android.framework.lang.CSId
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.toId

class CSListStoreEventProperty<T : CSId>(
    store: CSStoreInterface, key: String,
    val values: Iterable<T>, val default: List<T>, onChange: ((value: List<T>) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<List<T>>(store, key, onChange) {

    override var _value = load(store)

    override fun load(store: CSStoreInterface): List<T> {
        return store.get(key)?.split(",")
            ?.mapNotNull { categoryId -> values.find { it.id == categoryId } } ?: default
    }

    override fun save(store: CSStoreInterface, value: List<T>) =
        store.save(key, value.joinToString(",") { it.toId() })
}