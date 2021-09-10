package renetik.android.framework.store.property.nullable

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.preset.CSPresetStoreEventPropertyBase
import kotlin.reflect.KClass

class CSJsonTypeNullableStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<T?>(store, key, onApply) {
    override var _value: T? = load(store)
    override fun load(store: CSStoreInterface) = store.getJsonObject(key, type)
    override fun save(store: CSStoreInterface, value: T?) = store.save(key, value)
}