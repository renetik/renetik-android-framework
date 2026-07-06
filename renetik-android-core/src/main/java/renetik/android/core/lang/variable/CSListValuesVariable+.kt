package renetik.android.core.lang.variable

val <T : Enum<*>> CSListValuesVariable<T>.isLast get() = values.lastIndex == value.ordinal

val <T : Enum<*>> CSListValuesVariable<T>.next: T get() = values[value.ordinal + 1]

fun <T : Enum<*>> CSListValuesVariable<T>.next() {
    assign(next)
}

val <T : Enum<*>> CSListValuesVariable<T>.previous: T get() = values[value.ordinal - 1]

fun <T : Enum<*>> CSListValuesVariable<T>.previous() {
    assign(previous)
}

fun <T> CSListValuesVariable<T>.value(index: Int) {
    assign(values[index])
}