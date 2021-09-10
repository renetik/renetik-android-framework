package renetik.android.framework.store.property.late

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.json.data.extensions.getJsonListList
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonListListLateStoreEventProperty<T : CSJsonObject>(
    override val store: CSStoreInterface,
    override val key: String, val type: KClass<T>,
    onChange: ((value: List<List<T>>) -> Unit)? = null)
    : CSLateStoreEventProperty2<List<List<T>>>(store, key, onChange) {
    override fun load(): List<List<T>>? = store.getJsonListList(key, type)
    override fun save(value: List<List<T>>) = store.save(key, value)
}

