package renetik.android.framework.store.property.late

import renetik.android.framework.store.json.CSStoreJsonObject
import renetik.android.framework.store.CSStore
import kotlin.reflect.KClass

class CSJsonTypeLateStoreEventProperty<T : CSStoreJsonObject>(
    override val store: CSStore,
    override val key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSLateStoreEventProperty<T>(store, key, onChange) {
    override fun get(): T? = store.getJsonObject(key, type)
    override fun set(store: CSStore, value: T) = store.set(key, value)
}