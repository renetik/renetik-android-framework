package  renetik.android.json

import renetik.android.json.array.CSJsonArray
import renetik.android.json.obj.CSJsonObject
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

fun <T : CSJsonObject> KClass<T>.createJsonObject(map: Map<String, Any?>?): T =
    createInstance().apply { map?.let(::load) }

fun <T : CSJsonArray> KClass<T>.createJsonList(list: List<Any?>?): T =
    createInstance().apply { list?.let(::load) }

fun <T : CSJsonObject> KClass<T>.createJsonObjectList(data: List<Map<String, Any?>>?)
        : MutableList<T> = mutableListOf<T>().also { dataList ->
    data?.forEach { dataList += createJsonObject(it) }
}

fun <T : CSJsonObject> KClass<T>.createJsonObjectMap(
    data: Map<String, Map<String, Any?>>?)
        : MutableMap<String, T> = mutableMapOf<String, T>().also { dataList ->
    data?.forEach { dataList[it.key] = createJsonObject(it.value) }
}