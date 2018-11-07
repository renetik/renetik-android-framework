package cs.android.extensions

import cs.java.collections.CSMap

fun <K, V> MutableMap<K, V>.add(key: K, value: V): V {
    put(key, value)
    return value;
}