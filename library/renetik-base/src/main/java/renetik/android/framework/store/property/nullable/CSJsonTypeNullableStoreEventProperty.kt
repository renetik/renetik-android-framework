package renetik.android.framework.store.property.nullable

import renetik.android.framework.store.json.CSStoreJsonObject
import renetik.android.framework.store.CSStore
import kotlin.reflect.KClass

class CSJsonTypeNullableStoreEventProperty<T : CSStoreJsonObject>(
    store: CSStore, key: String, val type: KClass<T>,
    val default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSNullableStoreEventProperty<T>(store, key, default, listenStoreChanged = false, onApply) {
    override fun get(store: CSStore): T? = store.getJsonObject(key, type)
    override fun set(store: CSStore, value: T?) = store.set(key, value)
}