package renetik.android.framework.store.property.late

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStore
import kotlin.reflect.KClass

class CSJsonListLateStoreEventProperty<T : CSJsonObject>(
    override val store: CSStore,
    override val key: String, val type: KClass<T>,
    onChange: ((value: List<T>) -> Unit)? = null)
    : CSLateStoreEventProperty<List<T>>(store, key, onChange) {
    override fun get(): List<T>? = store.getJsonList(key, type)
    override fun set(store: CSStore, value: List<T>) = store.set(key, value)
}


