package renetik.android.framework.store.property.value

import renetik.android.framework.store.json.CSStoreJsonObject
import renetik.android.framework.store.CSStore
import kotlin.reflect.KClass

class CSJsonListValueStoreEventProperty<T : CSStoreJsonObject>(
    store: CSStore,
    key: String,
    val type: KClass<T>,
    val default: List<T> = emptyList(),
    listenStoreChanged: Boolean = false,
    onApply: ((value: List<T>) -> Unit)? = null
) : CSValueStoreEventProperty<List<T>>(store, key, listenStoreChanged, onApply) {
    override val defaultValue = default
    override var _value = load()
    override fun get(store: CSStore) = store.getJsonObjectList(key, type) ?: default
    override fun set(store: CSStore, value: List<T>) = store.set(key, value)
}