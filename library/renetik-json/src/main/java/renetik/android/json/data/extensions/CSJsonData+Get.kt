package  renetik.android.json.data.extensions

import renetik.android.json.data.CSJsonMap
import renetik.android.json.extensions.createJsonMap
import renetik.android.json.extensions.createJsonDataList
import kotlin.reflect.KClass

fun CSJsonMap.getString(key: String): String? = data[key]?.let { return it.toString() }

fun CSJsonMap.getDouble(key: String) = try {
    getString(key)?.toDouble()
} catch (e: NumberFormatException) {
    null
}

fun CSJsonMap.getLong(key: String) = try {
    getString(key)?.toLong()
} catch (e: NumberFormatException) {
    null
}

fun CSJsonMap.getInt(key: String) = try {
    getString(key)?.toInt()
} catch (e: NumberFormatException) {
    null
}

fun CSJsonMap.getBoolean(key: String) = try {
    getString(key)?.toBoolean()
} catch (e: NumberFormatException) {
    null
}

@Suppress("UNCHECKED_CAST")
fun CSJsonMap.getMap(key: String) = data[key] as? MutableMap<String, Any?>

@Suppress("UNCHECKED_CAST")
fun CSJsonMap.getList(key: String) = data[key] as? MutableList<Any?>

@Suppress("UNCHECKED_CAST")
fun CSJsonMap.getStringList(key: String): List<String>? = data[key] as? List<String>

@Suppress("UNCHECKED_CAST")
fun <T : CSJsonMap> CSJsonMap.get(type: KClass<T>, key: String) =
    (data[key] as? MutableMap<String, Any?>)?.let { type.createJsonMap(it) }

@Suppress("UNCHECKED_CAST")
fun <T : CSJsonMap> CSJsonMap.getList(type: KClass<T>, key: String) =
    (data[key] as? List<MutableMap<String, Any?>>)?.let { type.createJsonDataList(it) }