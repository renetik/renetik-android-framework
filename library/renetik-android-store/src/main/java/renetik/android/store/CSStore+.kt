package renetik.android.store

import renetik.android.core.kotlin.toId

fun <T> CSStore.getValue(key: String, values: Iterable<T>): T? {
    val savedString = get(key) ?: return null
    return values.find { it.toId() == savedString }
}

fun CSStore.getStringValue(key: String, default: String = "") =
    getString(key) ?: default

fun CSStore.getIntValue(key: String, default: Int = 0) =
    getInt(key) ?: default

fun CSStore.getBooleanValue(key: String, default: Boolean = false) =
    getBoolean(key) ?: default

@Suppress("UNCHECKED_CAST")
fun CSStore.getStringList(key: String): List<String>? = data[key] as? List<String>