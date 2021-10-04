package renetik.android.framework.store.property.preset

val <T : Enum<*>> CSListItemValueStoreEventProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListItemValueStoreEventProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListItemValueStoreEventProperty<T>.previous(): T = values[value.ordinal - 1]
fun <T> CSListItemValueStoreEventProperty<T>.value(index: Int) = value(values[index])