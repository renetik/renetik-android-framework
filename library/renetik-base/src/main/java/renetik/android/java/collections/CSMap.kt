package renetik.android.java.collections

fun <K, V> map() = CSMapImpl<K, V>()
fun <K, V> linkedMap() = CSLinkedMapImpl<K, V>()

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

interface CSMap<K, V> : Map<K, V>, MutableMap<K, V> {

    fun hasKey(key: K): Boolean

    fun hasValue(value: V): Boolean

    fun value(key: K): V?

}