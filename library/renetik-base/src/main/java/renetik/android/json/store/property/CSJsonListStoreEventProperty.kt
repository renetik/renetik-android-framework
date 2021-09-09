package renetik.android.json.store.property

import renetik.android.framework.lang.CSListInterface
import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.property.preset.CSPresetStoreEventPropertyBase
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.put
import renetik.android.json.data.CSJsonMapStore
import renetik.android.json.data.extensions.getList
import renetik.android.json.toJsonString
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class CSJsonListStoreEventProperty<T : CSJsonMapStore>(
    store: CSStoreInterface,
    key: String, private val createInstance: () -> T,
    val default: List<T> = emptyList(), onApply: ((value: List<T>) -> Unit)? = null
) : CSPresetStoreEventPropertyBase<List<T>>(store, key, onApply), CSListInterface<T> {

    constructor(
        store: CSStoreInterface, key: String,
        type: KClass<T>, default: List<T> = emptyList(),
        onApply: ((value: List<T>) -> Unit)? = null
    ) : this(store, key, { type.createInstance() }, default, onApply)

    override var _value = load(store)

    override fun load(store: CSStoreInterface): List<T> =
        store.getList(createInstance, key, default)

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

    fun save() = save(value)
}