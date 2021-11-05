package renetik.android.framework.store.property.value

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonListValueStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface,
    key: String,
    val type: KClass<T>,
    val default: List<T> = emptyList(),
    listenStoreChanged: Boolean = false,
    onApply: ((value: List<T>) -> Unit)? = null
) : CSValueStoreEventProperty<List<T>>(store, key, listenStoreChanged, onApply) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStoreInterface) = store.getJsonList(key, type) ?: default
    override fun set(store: CSStoreInterface, value: List<T>) = store.set(key, value)
}