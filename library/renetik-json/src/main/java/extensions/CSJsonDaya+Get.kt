package  renetik.android.json.extensions

import renetik.android.json.data.CSJsonData

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
//fun <T : CSJsonData> CSJsonData.load(dataValue: T, key: String): T? {
//    getMap(key)?.let { dataValue.load(it) } ?: return null
//    return dataValue
//}