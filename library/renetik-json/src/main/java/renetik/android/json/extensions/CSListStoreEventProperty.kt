package renetik.android.json.extensions

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.put
import renetik.android.json.data.CSJsonMap
import kotlin.reflect.KClass

class CSListStoreEventProperty<T : CSJsonMap>(
    val store: CSStoreInterface, val key: String,
    type: KClass<T>, default: List<T> = emptyList(), onApply: ((value: List<T>) -> Unit)? = null
) : CSEventProperty<List<T>>(store.loadList(type, key, default), onApply), Iterable<T> {

    override fun value(newValue: List<T>, fire: Boolean) {
        super.value(newValue, fire)
        save()
    }
    fun save() = store.save(key, value)

    override fun iterator() = value.iterator()
    val size get() = value.size
    fun put(element: T) = (value as MutableList<T>).put(element).apply { save() }
    fun remove(element: T) = (value as MutableList<T>).delete(element).apply { save() }
    fun clear() = apply { (value as MutableList<T>).clear() }
}