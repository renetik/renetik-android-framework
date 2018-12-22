package renetik.android.java.extensions.collections

fun <K, V> MutableMap<K, V>.add(key: K, value: V): V {
    put(key, value)
    return value;
}