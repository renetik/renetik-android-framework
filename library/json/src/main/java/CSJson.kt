package renetik.android.json

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import renetik.android.json.data.CSJsonData
import renetik.java.collections.CSList
import renetik.java.collections.CSMap
import renetik.java.collections.linkedMap
import renetik.java.collections.list
import renetik.java.extensions.createInstance
import kotlin.collections.set
import kotlin.reflect.KClass

fun <T : CSJsonData> createList(type: KClass<T>, dataList: List<CSMap<String, Any?>>?,
                                default: (List<T>)? = null): CSList<T> = list<T>().apply {
    dataList?.let {
        dataList.withIndex().forEach { (index, data) ->
            val item = put(type.createInstance()!!)
            item.index = index
            item.load(data)
        }
    } ?: let { default?.let { putAll(default) } }
}

fun <Type> fromJson(json: String): Type? {
    val value = JSONTokener(json).nextValue()
    @Suppress("UNCHECKED_CAST")
    return createValueFromJsonType(value) as Type
}

fun toJson(value: Any): String {
    return java.lang.String.valueOf(createJsonType(value))
}

fun toFormattedJson(value: Any): String {
    val jsonValue = createJsonType(value)
    if (jsonValue is JSONArray) return jsonValue.toString(4)
    if (jsonValue is JSONObject) return jsonValue.toString(4)
    return java.lang.String.valueOf(jsonValue)
}

fun toFormattedJson(value: Map<String, *>): String {
    return createJsonObject(value).toString(4)
}

fun toFormattedJson(value: List<*>): String {
    return createJsonArray(value).toString(4)
}

private fun createJsonArray(value: List<*>): JSONArray {
    val jsonArray = JSONArray()
    for (entry in value) jsonArray.put(createJsonType(entry!!))
    return jsonArray
}

private fun createJsonObject(value: Map<*, *>): JSONObject {
    val jsonObject = JSONObject()
    for (entry in value.entries) jsonObject.put(entry.key.toString(), createJsonType(entry.value!!))
    return jsonObject
}

private fun createJsonType(value: Any?): Any {
    if (value is Number || value is String || value is Boolean) return value
    if (value is Map<*, *>) return createJsonObject(value)
    if (value is List<*>) return createJsonArray(value)
    if (value is CSJsonDataMap) return createJsonObject(value.getJsonDataMap())
    if (value is CSJsonDataList) return createJsonArray(value.getJsonDataList())
    return java.lang.String.valueOf(value)
}

private fun createValueFromJsonType(value: Any?): Any? {
    if (value is Number || value is String || value is Boolean) return value
    if (value is JSONObject) return createMapObject(value)
    if (value is JSONArray) return createListObject(value)
    return null
}

fun createListObject(jsonArray: JSONArray): List<Any?> {
    val list: CSList<Any?> = list()
    for (index in 0 until jsonArray.length()) list.add(createValueFromJsonType(jsonArray[index]))
    return list
}

fun createMapObject(jsonObject: JSONObject): Map<String, Any?> {
    val map: CSMap<String, Any?> = linkedMap()
    for (key in jsonObject.keys()) map[key] = createValueFromJsonType(jsonObject[key])
    return map
}

interface CSJsonDataMap {
    fun getJsonDataMap(): Map<String, *>
}

interface CSJsonDataList {
    fun getJsonDataList(): List<*>
}