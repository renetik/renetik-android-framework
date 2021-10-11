package renetik.android.framework.store.property.value

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass

class CSJsonListValueStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface,
    key: String,
    val type: KClass<T>,
    val default: List<T> = emptyList(),
    onApply: ((value: List<T>) -> Unit)? = null
) : CSValueStoreEventProperty<List<T>>(store, key, default, onApply) {

    override var _value = firstLoad()
    override fun load(store: CSStoreInterface) = store.getJsonList(key, type) ?: default
    override fun save(store: CSStoreInterface, value: List<T>) = store.save(key, value)

    fun edit(function: (MutableList<T>) -> Unit) {
        function((value as MutableList<T>))
        save(store, value)
    }
}