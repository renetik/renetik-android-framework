package renetik.android.framework.store

import renetik.android.java.extensions.toId

fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>): T? {
    val savedString = get(key) ?: return null
    return values.find { it.toId() == savedString }
}