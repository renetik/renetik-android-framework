package renetik.android.json.store.property

import renetik.android.framework.event.property.CSEventPropertyImpl
import renetik.android.framework.lang.CSListInterface
import renetik.android.framework.store.CSStoreInterface
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.put
import renetik.android.json.data.CSJsonMap
import renetik.android.json.store.loadList
import renetik.android.json.store.save
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class CSListStoreEventProperty<T : CSJsonMap>(
    val store: CSStoreInterface, val key: String, createInstance: () -> T,
    default: List<T> = emptyList(), onApply: ((value: List<T>) -> Unit)? = null
) : CSEventPropertyImpl<List<T>>(store.loadList(createInstance, key, default), onApply),
    CSListInterface<T> {

    constructor(
        store: CSStoreInterface, key: String,
        type: KClass<T>, default: List<T> = emptyList(), onApply: ((value: List<T>) -> Unit)? = null
    ) : this(store, key, { type.createInstance() }, default, onApply)

    override fun onValueChanged(newValue: List<T>) {
        super.onValueChanged(newValue)
        save()
    }

    fun save() = store.save(key, value)

    override fun iterator() = value.iterator()

    override val size get() = value.size

    override fun put(item: T) = (value as MutableList<T>).put(item).apply { save() }

    override fun delete(item: T): T {
        (value as MutableList<T>).delete(item)
        save()
        return item
    }

    override fun removeAll() = apply { (value as MutableList<T>).clear() }
}