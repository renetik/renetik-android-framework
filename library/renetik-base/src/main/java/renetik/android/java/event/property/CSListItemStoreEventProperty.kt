package renetik.android.java.event.property

import renetik.android.framework.CSValueStoreInterface
import renetik.android.framework.getValue

class CSListItemStoreEventProperty<T>(
    val store: CSValueStoreInterface, val key: String, val values: List<T>,
    val default: T, onApply: ((value: T) -> Unit)? = null
) : CSEventProperty<T>(store.getValue(key, values, default), onApply) {
    override fun value(newValue: T, fire: Boolean) {
        super.value(newValue, fire)
        store.save(key, newValue.hashCode())
    }
}

val <T : Enum<*>> CSListItemStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]