package  renetik.android.json.data.extensions

import renetik.android.json.data.CSJsonData
import renetik.android.json.extensions.createJsonData
import renetik.android.json.extensions.createJsonDataList
import kotlin.reflect.KClass

fun CSJsonData.getString(key: String): String? = data[key]?.let { return it.toString() }
fun CSJsonData.getDouble(key: String) = try {
    getString(key)?.toDouble()
} catch (e: NumberFormatException) {
    null
}

fun CSJsonData.getLong(key: String) = try {
    getString(key)?.toLong()
} catch (e: NumberFormatException) {
    null
}

fun CSJsonData.getInt(key: String) = try {
    getString(key)?.toInt()
} catch (e: NumberFormatException) {
    null
}

fun CSJsonData.getBoolean(key: String) = try {
    getString(key)?.toBoolean()
} catch (e: NumberFormatException) {
    null
}

@Suppress("UNCHECKED_CAST")
fun CSJsonData.getMap(key: String) = data[key] as? MutableMap<String, Any?>

@Suppress("UNCHECKED_CAST")
fun CSJsonData.getList(key: String) = data[key] as? MutableList<Any?>

@Suppress("UNCHECKED_CAST")
fun CSJsonData.getStringList(key: String): List<String>? = data[key] as? List<String>

@Suppress("UNCHECKED_CAST")
fun <T : CSJsonData> CSJsonData.get(type: KClass<T>, key: String) =
    (data[key] as? MutableMap<String, Any?>)?.let { type.createJsonData(it) }

@Suppress("UNCHECKED_CAST")
fun <T : CSJsonData> CSJsonData.getList(type: KClass<T>, key: String) =
    (data[key] as? List<MutableMap<String, Any?>>)?.let { type.createJsonDataList(it) }