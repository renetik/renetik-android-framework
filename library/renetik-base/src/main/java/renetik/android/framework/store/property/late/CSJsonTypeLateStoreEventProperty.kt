package renetik.android.framework.store.property.late

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonTypeLateStoreEventProperty<T : CSJsonObject>(
    override val store: CSStoreInterface,
    override val key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSLateStoreEventProperty2<T>(store, key, onChange) {
    override fun load(): T? = store.getJsonObject(key, type)
    override fun save(value: T) = store.save(key, value)
}