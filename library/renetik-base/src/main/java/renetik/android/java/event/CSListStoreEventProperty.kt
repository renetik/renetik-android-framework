package renetik.android.java.event

import renetik.android.framework.CSValueStoreInterface
import renetik.android.framework.getValue

class CSListStoreEventProperty<T>(
    store: CSValueStoreInterface, val key: String, val values: List<T>,
    val default: T, onApply: ((value: T) -> Unit)? = null)
    : CSEventProperty<T>(store.getValue(key, values, default), onApply) {

    init {
        onChange {
            store.save(key, it.hashCode())
        }
    }

    var store: CSValueStoreInterface = store
        set(value) {
            field = value
            value(store.getValue(key, values, default), true)
        }
}

val <T : Enum<*>> CSListStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]