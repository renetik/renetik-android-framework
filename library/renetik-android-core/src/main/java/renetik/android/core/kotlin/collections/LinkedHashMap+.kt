package renetik.android.core.kotlin.collections

fun <K, V> linkedMap() = LinkedHashMap<K, V>()
fun <K, V> LinkedHashMap<K, V>.getValue(index: Int) = values.mutable()[index]
fun <K, V> LinkedHashMap<K, V>.index(key: K) = keys.indexOf(key)
fun <K, V> LinkedHashMap<K, V>.removeAt(i: Int) = remove(keys.mutable()[i])

fun <K, V> LinkedHashMap<K, V>.deleteLast(): V? = remove(entries.last().key)

fun <V> MutableSet<V>.deleteLast(): V? = this.lastOrNull().also { remove(it) }