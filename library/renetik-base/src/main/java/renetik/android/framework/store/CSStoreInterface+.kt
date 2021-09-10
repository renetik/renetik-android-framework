package renetik.android.framework.store

import renetik.kotlin.toId

fun <T> CSStoreInterface.getValue(key: String, values: Iterable<T>): T? {
    val savedString = get(key) ?: return null
    return values.find { it.toId() == savedString }
}

fun CSStoreInterface.getStringValue(key: String, default: String = "") =
    getString(key) ?: default

fun CSStoreInterface.getIntValue(key: String, default: Int = 0) =
    getInt(key) ?: default

fun CSStoreInterface.getBooleanValue(key: String, default: Boolean = false) =
    getBoolean(key) ?: default

@Suppress("UNCHECKED_CAST")
fun CSStoreInterface.getStringList(key: String): List<String>? = data[key] as? List<String>