package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventProperty

class CSListItemStoreEventProperty<T>(
    private var store: CSStoreInterface, private val key: String,
    val values: List<T>, val default: T, onChange: ((value: T) -> Unit)? = null
) : CSEventProperty<T>(store.getValue(key, values, default), onChange) {
    override fun value(newValue: T, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue.toString())
    }

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getValue(key, values, default))
}

private fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>, default: T): T {
    val savedString = get(key) ?: return default
    return values.find { it.toString() == savedString } ?: default
}

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]