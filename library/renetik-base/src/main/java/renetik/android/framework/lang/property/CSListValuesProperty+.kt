package renetik.android.framework.lang.property

val <T : Enum<*>> CSListValuesProperty<T>.isLast get() = values.lastIndex == value.ordinal
fun <T : Enum<*>> CSListValuesProperty<T>.next(): T = values[value.ordinal + 1]
fun <T : Enum<*>> CSListValuesProperty<T>.previous(): T = values[value.ordinal - 1]
fun <T> CSListValuesProperty<T>.value(index: Int) {
    value = values[index]
}