package renetik.android.java.extensions.collections

fun <K, V> map() = HashMap<K, V>()
fun map(vararg values: Any) = map<Any, Any>().apply {
    var i = 0
    while (i < values.size) {
        put(values[i], values[i + 1])
        i += 2
    }
}

fun map(vararg values: String) = map<String, String>().apply {
    var i = 0
    while (i < values.size) {
        put(values[i], values[i + 1])
        i += 2
    }
}

fun <K, V> HashMap<K, V>.hasKey(key: K) = containsKey(key)
fun <K, V> HashMap<K, V>.hasValue(value: V) = containsValue(value)
fun <K, V> HashMap<K, V>.value(key: K) = get(key)