package renetik.android.framework.store.property.value

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonTypeValueStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T, onApply: ((value: T) -> Unit)? = null
) : CSValueStoreEventProperty<T>(store, key, default, onApply) {
    override var _value: T = firstLoad()
    override fun load(store: CSStoreInterface) = store.getJsonObject(key, type) ?: default
    override fun save(store: CSStoreInterface, value: T) =
        store.save(key, value)
}