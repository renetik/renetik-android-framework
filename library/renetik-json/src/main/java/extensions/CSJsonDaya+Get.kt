package  renetik.android.json.extensions

import renetik.android.java.extensions.isEmpty
import renetik.android.json.data.CSJsonData
import renetik.android.json.parseJson
import kotlin.reflect.KClass

fun CSJsonData.getString(key: String): String? = data()[key]?.let { return it.toString() }
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

fun CSJsonData.getMap(key: String) = data()[key] as? MutableMap<String, Any?>
fun CSJsonData.getList(key: String) = data()[key] as? MutableList<Any?>

fun <T : CSJsonData> CSJsonData.get(type: KClass<T>, key: String) =
    type.createJsonData(getJson(key))

fun <T : CSJsonData> CSJsonData.getList(type: KClass<T>, key: String) =
    type.createJsonDataList(getJson(key))

private fun <Type> CSJsonData.getJson(key: String): Type? {
    val loadString = getString(key)
    return if (loadString.isEmpty) null else loadString!!.parseJson<Type>()
}