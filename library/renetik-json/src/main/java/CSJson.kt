package renetik.android.json

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import renetik.android.java.collections.CSMap
import renetik.android.java.extensions.collections.linkedMap
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll
import renetik.android.java.extensions.createInstance
import renetik.android.json.data.CSJsonData
import kotlin.reflect.KClass

fun <T : CSJsonData> createJsonDataType(type: KClass<T>, data: CSMap<String, Any?>?) =
        type.createInstance()!!.apply { data?.let { load(it) } }

fun <T : CSJsonData> createJsonDataList(type: KClass<T>, dataList: List<CSMap<String, Any?>>?,
                                        default: (List<T>)? = null) = list<T>().apply {
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

fun createJsonArray(value: List<*>): JSONArray {
    val jsonArray = JSONArray()
    for (entry in value) jsonArray.put(createJsonType(entry!!))
    return jsonArray
}

fun createJsonObject(value: Map<*, *>): JSONObject {
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
    val list = list<Any?>()
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