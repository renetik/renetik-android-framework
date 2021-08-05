package  renetik.android.json.extensions

import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.putAll
import renetik.android.java.extensions.notNull
import renetik.android.json.data.CSJsonList
import renetik.android.json.data.CSJsonMap
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

fun <T : CSJsonMap> KClass<T>.createJsonMap(map: Map<String, Any?>?) =
    createInstance().apply { map.notNull { load(it) } }

fun <T : CSJsonList> KClass<T>.createJsonList(list: List<Any?>?) =
    createInstance().apply { list.notNull { load(it) } }

fun <T : CSJsonMap> KClass<T>.createJsonDataList(
    data: List<MutableMap<String, Any?>>?,
    defaultDataList: (List<T>)? = null
): MutableList<T> = list<T>().also { dataList ->
    if (data != null)
        data.withIndex().forEach { (index, map) ->
            dataList.put(createJsonMap(map)).index = index
        }
    else defaultDataList?.let { dataList.putAll(defaultDataList) }
}