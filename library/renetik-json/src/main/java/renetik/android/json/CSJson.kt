package renetik.android.json

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import renetik.android.java.common.catchWarnReturnNull
import renetik.android.java.extensions.asString
import renetik.android.java.extensions.collections.linkedMap
import renetik.android.java.extensions.collections.list

fun String.parseJsonMap() = parseJson<MutableMap<String, Any?>>()

fun String.parseJsonList() = parseJson<MutableList<Any?>>()

@Suppress("UNCHECKED_CAST")
fun <Type> String.parseJson(): Type? =
    catchWarnReturnNull<Any, JSONException> {
        JSONTokener(this).nextValue()
    }.createValueFromJsonType() as? Type

fun Any.toJsonString(formatted: Boolean = false): String {
    val jsonType = toJsonType()
    if (formatted) {
        if (jsonType is JSONArray) return jsonType.toString(4)
        if (jsonType is JSONObject) return jsonType.toString(4)
    }
    return java.lang.String.valueOf(jsonType)
}

@Suppress("UNCHECKED_CAST")
private fun Any?.toJsonType(): Any? {
    if (this is Number || this is String || this is Boolean) return this
    return (this as? Map<String, *>)?.toJSONObject()
        ?: (this as? List<*>)?.toJSONArray()
        ?: (this as? CSJsonMapInterface)?.asStringMap()?.toJSONObject()
        ?: (this as? CSJsonListInterface)?.asList()?.toJSONArray()
        ?: this?.asString()
}

fun List<*>.toJSONArray(): JSONArray {
    val jsonArray = JSONArray()
    for (entry in this) jsonArray.put(entry!!.toJsonType())
    return jsonArray
}

fun Map<String, *>.toJSONObject(): JSONObject {
    val jsonObject = JSONObject()
    for (entry in entries)
        jsonObject.put(entry.key, entry.value!!.toJsonType())
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

interface CSJsonMapInterface {
    fun asStringMap(): Map<String, *>
}

interface CSJsonListInterface {
    fun asList(): List<*>
}