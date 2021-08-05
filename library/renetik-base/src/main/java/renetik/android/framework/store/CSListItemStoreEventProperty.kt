package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyImpl

class CSListItemStoreEventProperty<T>(
    private var store: CSStoreInterface, private val key: String,
    val values: List<T>, val default: T, onChange: ((value: T) -> Unit)? = null
) : CSEventPropertyImpl<T>(store.getValue(key, values, default), onChange) {
    override fun onValueChanged(newValue: T) = setValue(store, newValue)
    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }
    fun reload() = value(getValue(store))

    // CSStoreEventPropertyInterface
    fun setValue(store: CSStoreInterface, value: T) = store.save(key, value.toString())
    fun getValue(store: CSStoreInterface) = store.getValue(key, values, default)
}

fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>, default: T): T {
    val savedString = get(key) ?: return default
    return values.find { it.toString() == savedString } ?: default
}

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]