package renetik.android.store.property.late

import renetik.android.store.json.CSStoreJsonObject
import renetik.android.store.json.getJsonListList
import renetik.android.store.CSStore
import kotlin.reflect.KClass

class CSJsonListListLateStoreEventProperty<T : CSStoreJsonObject>(
    override val store: CSStore,
    override val key: String, val type: KClass<T>,
    onChange: ((value: List<List<T>>) -> Unit)? = null)
    : CSLateStoreEventProperty<List<List<T>>>(store, key, onChange) {
    override fun get(): List<List<T>>? = store.getJsonListList(key, type)
    override fun set(store: CSStore, value: List<List<T>>) = store.set(key, value)
}

