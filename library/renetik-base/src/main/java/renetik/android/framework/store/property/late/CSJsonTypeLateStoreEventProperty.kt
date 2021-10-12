package renetik.android.framework.store.property.late

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonTypeLateStoreEventProperty<T : CSJsonObject>(
    override val store: CSStoreInterface,
    override val key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSLateStoreEventProperty<T>(store, key, onChange) {
    override fun get(): T? = store.getJsonObject(key, type)
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value)
}