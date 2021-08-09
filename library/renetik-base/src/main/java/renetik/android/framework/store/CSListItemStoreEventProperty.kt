package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase
import renetik.android.framework.lang.CSId

class CSListItemStoreEventProperty<T>(
    store: CSStoreInterface, private val key: String,
    val values: List<T>, val default: T, onChange: ((value: T) -> Unit)? = null
) : CSStoreEventPropertyBase<T>(store, onChange) {
    override var _value = load(store)
    override fun load(store: CSStoreInterface) = store.getValue(key, values, default)
    override fun save(store: CSStoreInterface, value: T) = store.save(key, value.toId())
}

//TODO!!! Can be inlined ?
fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>, default: T): T {
    val savedString = get(key) ?: return default
    return values.find { it.toId() == savedString } ?: default
}

private fun Any?.toId() = (this as? CSId)?.id ?: this.toString()

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]