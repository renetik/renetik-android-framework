package  renetik.android.json.extensions

import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll
import renetik.android.java.extensions.createInstance
import renetik.android.java.extensions.notNull
import renetik.android.json.data.CSJsonData
import kotlin.reflect.KClass


fun <T : CSJsonData> KClass<T>.createJsonData(map: MutableMap<String, Any?>?) =
    createInstance()!!.apply { map.notNull { load(it) } }

fun <T : CSJsonData> KClass<T>.createJsonDataList(
    data: List<MutableMap<String, Any?>>?,
    defaultDataList: (List<T>)? = null
): MutableList<T> = list<T>().also { dataList ->
    if (data != null) data.withIndex().forEach { (index, map) ->
        dataList.put(createJsonData(map)).index = index
    }
    else defaultDataList?.let { dataList.putAll(defaultDataList) }
}