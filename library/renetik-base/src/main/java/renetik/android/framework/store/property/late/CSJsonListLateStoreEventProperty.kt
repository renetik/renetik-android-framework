package renetik.android.framework.store.property.late

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonListLateStoreEventProperty<T : CSJsonObject>(
    override val store: CSStoreInterface,
    override val key: String, val type: KClass<T>,
    onChange: ((value: List<T>) -> Unit)? = null)
    : CSLateStoreEventProperty<List<T>>(store, key, onChange) {
    override fun load(): List<T>? = store.getJsonList(key, type)
    override fun save(store: CSStoreInterface, value: List<T>) = store.save(key, value)
}


