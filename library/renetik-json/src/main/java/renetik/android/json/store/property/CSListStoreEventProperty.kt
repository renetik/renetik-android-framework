package renetik.android.json.store.property

import renetik.android.framework.event.property.CSStoreEventPropertyBase
import renetik.android.framework.lang.CSListInterface
import renetik.android.framework.store.CSStoreInterface
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll
import renetik.android.json.data.CSJsonMap
import renetik.android.json.parseJson
import renetik.android.json.toJsonString
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class CSListStoreEventProperty<T : CSJsonMap>(
    store: CSStoreInterface,
    key: String, private val createInstance: () -> T,
    val default: List<T> = emptyList(), onApply: ((value: List<T>) -> Unit)? = null
) : CSStoreEventPropertyBase<List<T>>(store, key, onApply),
    CSListInterface<T> {

    constructor(
        store: CSStoreInterface, key: String,
        type: KClass<T>, default: List<T> = emptyList(), onApply: ((value: List<T>) -> Unit)? = null
    ) : this(store, key, { type.createInstance() }, default, onApply)

    override var _value = load(store)

    override fun load(store: CSStoreInterface): List<T> =
        store.loadList(createInstance, key, default)

    override fun save(store: CSStoreInterface, value: List<T>) =
        store.save(key, value.toJsonString())

    override fun iterator() = value.iterator()

    override val size get() = value.size

    override fun put(item: T) = (value as MutableList<T>).put(item).apply { save() }

    override fun delete(item: T): T {
        (value as MutableList<T>).delete(item)
        save()
        return item
    }

    override fun removeAll() = apply { (value as MutableList<T>).clear() }

    fun save() = save(store, value)
}

private fun <T : CSJsonMap> CSStoreInterface.loadList(
    createInstance: () -> T, key: String, default: List<T>? = null): MutableList<T> {
    val list = list<T>()
    val data = get(key)?.parseJson<List<Map<String, Any?>>?>()
    data?.withIndex()?.forEach { (index, itemData) ->
        val itemInstance = createInstance()
        itemInstance.load(itemData)
        itemInstance.index = index
        list.put(itemInstance)
    } ?: default?.let { list.putAll(default) }
    return list
}