package renetik.android.store.property.late

import renetik.android.store.json.CSStoreJsonObject
import renetik.android.store.CSStore
import kotlin.reflect.KClass

class CSJsonListLateStoreEventProperty<T : CSStoreJsonObject>(
    override val store: CSStore,
    override val key: String, val type: KClass<T>,
    onChange: ((value: List<T>) -> Unit)? = null)
    : CSLateStoreEventProperty<List<T>>(store, key, onChange) {
    override fun get(): List<T>? = store.getJsonObjectList(key, type)
    override fun set(store: CSStore, value: List<T>) = store.set(key, value)
}


