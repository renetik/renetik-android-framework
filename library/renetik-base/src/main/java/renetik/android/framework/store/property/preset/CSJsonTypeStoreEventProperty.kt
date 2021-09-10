package renetik.android.framework.store.property.preset

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonTypeStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T, onApply: ((value: T) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<T>(store, key, onApply) {
    override var _value: T = load(store)

    override fun load(store: CSStoreInterface) =
        store.getJsonObject(key, type) ?: default

    override fun save(store: CSStoreInterface, value: T) =
        store.save(key, value)
}