package renetik.android.json

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import renetik.android.java.extensions.collections.linkedMap
import renetik.android.java.extensions.collections.list

fun <Type> String.parseJson(): Type? {
    val value = JSONTokener(this).nextValue()
    @Suppress("UNCHECKED_CAST")
    return value.createValueFromJsonType() as Type
}

fun Any.toJsonString(formatted: Boolean = false): String {
    val jsonType = toJsonType()
    if (formatted) {
        if (jsonType is JSONArray) return jsonType.toString(4)
        if (jsonType is JSONObject) return jsonType.toString(4)
    }
    return java.lang.String.valueOf(jsonType)
}

private fun Any?.toJsonType(): Any {
    if (this is Number || this is String || this is Boolean) return this
    if (this is Map<*, *>) return toJSONObject()
    if (this is List<*>) return toJSONArray()
    if (this is CSJsonMap) return asJsonMap().toJSONObject()
    if (this is CSJsonList) return asJsonList().toJSONArray()
    return java.lang.String.valueOf(this)
}

private fun List<*>.toJSONArray(): JSONArray {
    val jsonArray = JSONArray()
    for (entry in this) jsonArray.put(entry!!.toJsonType())
    return jsonArray
}

fun Map<*, *>.toJSONObject(): JSONObject {
    val jsonObject = JSONObject()
    for (entry in entries)
        jsonObject.put(entry.key.toString(), entry.value!!.toJsonType())
    return jsonObject
}

private fun Any?.createValueFromJsonType(): Any? {
    if (this is Number || this is String || this is Boolean) return this
    if (this is JSONObject) return createMapObject()
    if (this is JSONArray) return createListObject()
    return null
}

private fun JSONArray.createListObject(): List<Any?> {
    val list = list<Any?>()
    for (index in 0 until length()) list.add(this[index].createValueFromJsonType())
    return list
}

private fun JSONObject.createMapObject(): Map<String, Any?> {
    val map = linkedMap<String, Any?>()
    for (key in keys()) map[key] = this[key].createValueFromJsonType()
    return map
}

interface CSJsonMap {
    fun asJsonMap(): Map<String, *>
}

interface CSJsonList {
    fun asJsonList(): List<*>
}