package  renetik.android.framework.json.extensions

import renetik.android.framework.json.data.CSJsonList
import renetik.android.framework.json.data.CSJsonObject
import renetik.kotlin.collections.list
import renetik.kotlin.notNull
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

fun <T : CSJsonObject> KClass<T>.createJsonObject(map: Map<String, Any?>?) =
    createInstance().apply { map.notNull { load(it) } }

fun <T : CSJsonList> KClass<T>.createJsonList(list: List<Any?>?) =
    createInstance().apply { list.notNull { load(it) } }

fun <T : CSJsonObject> KClass<T>.createJsonObjectList(data: List<Map<String, Any?>>?)
        : MutableList<T> = list<T>().also { dataList ->
    data?.forEach { dataList.put(createJsonObject(it)) }
}