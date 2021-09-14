package renetik.android.framework.store.property.preset

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.save
import kotlin.reflect.KClass

class CSJsonListStoreEventProperty<T : CSJsonObject>(
    store: CSStoreInterface, key: String,
    val type: KClass<T>, val default: List<T> = emptyList(),
    onApply: ((value: List<T>) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<List<T>>(store, key, onApply) {

    override var _value = load(store)

    override fun load(store: CSStoreInterface): List<T> =
        store.getJsonList(key, type) ?: default

    override fun save(store: CSStoreInterface, value: List<T>) =
        store.save(key, value)

    fun edit(function: (MutableList<T>) -> Unit) {
        function((value as MutableList<T>))
        save(value)
    }
}