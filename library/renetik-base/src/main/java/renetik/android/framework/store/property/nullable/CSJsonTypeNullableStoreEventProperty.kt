package renetik.android.framework.store.property.nullable

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonTypeNullableStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSNullableStoreEventProperty<T>(store, key, default, onApply) {
    override fun get(store: CSStoreInterface): T? = store.getJsonObject(key, type)
    override fun set(store: CSStoreInterface, value: T?) = store.set(key, value)
}