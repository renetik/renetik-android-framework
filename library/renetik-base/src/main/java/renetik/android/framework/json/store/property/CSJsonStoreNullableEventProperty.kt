package renetik.android.framework.json.store.property

import renetik.android.framework.store.property.preset.CSPresetStoreEventPropertyBase
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.extensions.createJsonMap
import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJsonString
import kotlin.reflect.KClass

class CSJsonStoreNullableEventProperty<T : CSJsonMapStore>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T? = null, onApply: ((value: T?) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<T?>(store, key, onApply) {
    override var _value: T? = load(store)

    override fun load(store: CSStoreInterface): T? {
        val data = store.get(key)?.parseJsonMap() ?: return default
        return type.createJsonMap(data)
    }

    override fun save(store: CSStoreInterface, value: T?) =
        store.save(key, value?.toJsonString())
}

class CSJsonStoreEventProperty<T : CSJsonMapStore>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    val default: T, onApply: ((value: T) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<T>(store, key, onApply) {
    override var _value: T = load(store)

    override fun load(store: CSStoreInterface): T {
        val data = store.get(key)?.parseJsonMap() ?: return default
        return type.createJsonMap(data)
    }

    override fun save(store: CSStoreInterface, value: T) =
        store.save(key, value.toJsonString())
}