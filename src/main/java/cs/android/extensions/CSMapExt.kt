package cs.android.extensions

import cs.java.collections.CSMap

public operator fun <K, V> CSMap<K, V>.set(key: K, value: V): V {
    put(key, value)
    return value;
}