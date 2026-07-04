package renetik.android.store.property.late

import renetik.android.json.obj.getJsonListList
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

// ! Not listening and saving data changes , shall we also here ?
class CSJsonListListLateStoreProperty<T : CSJsonObjectStore>(
    store: CSStore, override val key: String, val type: KClass<T>,
    onChange: ((value: List<List<T>>) -> Unit)? = null
) : CSLateStorePropertyBase<List<List<T>>>(store, key, onChange) {
    override fun get(store: CSStore): List<List<T>>? = store.getJsonListList(key, type)
    override fun set(store: CSStore, value: List<List<T>>) = store.set(key, value)
}

