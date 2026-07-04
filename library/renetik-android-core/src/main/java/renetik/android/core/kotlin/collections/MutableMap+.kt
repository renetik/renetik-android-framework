package renetik.android.core.kotlin.collections

fun <K, V> MutableMap<K, V>.add(key: K, value: V): V {
    put(key, value)
    return value
}

fun <K, V> MutableMap<K, V>.reload(map: MutableMap<K, V>) = apply {
    clear()
    putAll(map)
}

fun <K, V> MutableMap<K, V>.removeFirst(condition: (K, V) -> Boolean) =
    iterator().removeFirst { condition(it.key, it.value) }

fun <K, V> MutableMap<K, V>.removeValue(toRemove: V) =
    removeFirst { _, value -> value == toRemove }

fun <K, V> MutableMap<K, V>.removeKey(toRemove: K) =
    removeFirst { key, _ -> key == toRemove }