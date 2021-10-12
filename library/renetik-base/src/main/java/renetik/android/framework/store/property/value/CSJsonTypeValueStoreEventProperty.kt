package renetik.android.framework.store.property.value

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.reflect.createInstance
import kotlin.reflect.KClass

class CSJsonTypeValueStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface, key: String, val type: KClass<T>,
    onApply: ((value: T) -> Unit)? = null
) : CSValueStoreEventProperty<T>(store, key, onApply) {
    override val defaultValue get() = type.createInstance()!!
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getJsonObject(key, type) ?: defaultValue
    override fun set(store: CSStoreInterface, value: T) = store.set(key, value)
}