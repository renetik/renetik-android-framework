package renetik.android.framework.store

import renetik.android.framework.lang.CSId

class CSListItemStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T>, val default: T, onChange: ((value: T) -> Unit)? = null
) : CSStoreEventPropertyBase<T>(store, key, onChange) {
    override var _value = load(store)
    override fun load(store: CSStoreInterface) = store.getValue(key, values, default)
    override fun save(store: CSStoreInterface, value: T) = store.save(key, value.toId())
}

fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>, default: T): T {
    val savedString = get(key) ?: return default
    return values.find { it.toId() == savedString } ?: default
}

private fun Any?.toId() = (this as? CSId)?.id ?: this.toString()

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]


class CSListItemsStoreEventProperty<T : CSId>(
    store: CSStoreInterface, key: String,
    val values: Iterable<T>, val default: List<T>, onChange: ((value: List<T>) -> Unit)? = null
) : CSStoreEventPropertyBase<List<T>>(store, key, onChange) {

    override var _value = load(store)

    override fun load(store: CSStoreInterface): List<T> {
        return store.get(key)?.split(",")
            ?.mapNotNull { categoryId -> values.find { it.id == categoryId } } ?: default
    }

    override fun save(store: CSStoreInterface, value: List<T>) =
        store.save(key, value.joinToString(",") { it.toId() })
}