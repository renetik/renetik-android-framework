package renetik.android.framework.store.property.value

import renetik.android.framework.store.CSStoreInterface
import renetik.android.framework.store.getValue
import renetik.android.framework.store.property.value.CSValueStoreEventProperty
import renetik.android.java.extensions.toId

class CSListItemStoreEventProperty<T>(
    store: CSStoreInterface, key: String,
    val values: List<T>, default: T, onChange: ((value: T) -> Unit)? = null
) : CSValueStoreEventProperty<T>(store, key, default, onChange) {
    override var _value = firstLoad()
    override fun loadNullable(store: CSStoreInterface) = store.getValue(key, values)
    override fun save(store: CSStoreInterface, value: T) = store.save(key, value.toId())
}

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]