package renetik.android.json.store.property

import renetik.android.framework.event.property.CSStoreEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.json.data.CSJsonMap
import renetik.android.json.extensions.createJsonMap
import renetik.android.json.parseJsonMap
import renetik.android.json.toJsonString
import kotlin.reflect.KClass

class CSItemStoreEventProperty<T : CSJsonMap>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSStoreEventPropertyBase<T?>(store, key, onApply) {
    override var _value: T? = load(store)

    @Suppress("UNCHECKED_CAST")
    override fun load(store: CSStoreInterface): T? {
        val data = store.get(key)?.parseJsonMap() ?: return default
        return type.createJsonMap(data)
    }

    override fun save(store: CSStoreInterface, value: T?) =
        store.save(key, value?.toJsonString())
}