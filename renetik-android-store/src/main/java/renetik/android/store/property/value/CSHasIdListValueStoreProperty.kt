package renetik.android.store.property.value

import renetik.android.core.kotlin.toId
import renetik.android.core.lang.CSHasId
import renetik.android.json.obj.getStringList
import renetik.android.store.CSStore

class CSHasIdListValueStoreProperty<T : CSHasId>(
    store: CSStore, key: String,
    val values: Iterable<T>,
    override val default: List<T> = emptyList(),
    val storedAsStringList: Boolean = false,
    onChange: ((value: List<T>) -> Unit)? = null
) : CSValueStoreProperty<List<T>>(store, key, onChange) {

    override fun get(store: CSStore): List<T> = store.getStringList()
        ?.mapNotNull { id -> values.find { it.id == id } } ?: this.default

    private fun CSStore.getStringList() =
        if (storedAsStringList) getStringList(key) else getString(key)?.split(",")

    override fun set(store: CSStore, value: List<T>) {
        if (storedAsStringList) store.set(key, value.map { it.toId() })
        else store.set(key, value.joinToString(",") { it.toId() })
    }
}