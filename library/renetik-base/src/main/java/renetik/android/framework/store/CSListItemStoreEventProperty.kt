package renetik.android.framework.store

import renetik.android.framework.event.property.CSStoreEventPropertyBase

class CSListItemStoreEventProperty<T>(
    store: CSStoreInterface, private val key: String,
    val values: List<T>, val default: T, onChange: ((value: T) -> Unit)? = null
) : CSStoreEventPropertyBase<T>(store, onChange) {
    override var _value = getValue(store)
    override fun getValue(store: CSStoreInterface) = store.getValue(key, values, default)
    override fun setValue(store: CSStoreInterface, value: T) = store.save(key, value.toString())
}

//TODO!!! Can be inlined ?
fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>, default: T): T {
    val savedString = get(key) ?: return default
    return values.find { it.toString() == savedString } ?: default
}

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]