package  renetik.android.json.extensions

import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.collections.putAll
import renetik.android.java.extensions.createInstance
import renetik.android.json.data.CSJsonData
import kotlin.reflect.KClass


fun <T : CSJsonData> KClass<T>.createJsonData(map: MutableMap<String, Any?>?) =
        createInstance()!!.apply { map?.let { load(it) } }

fun <T : CSJsonData> KClass<T>.createJsonDataList(
        list: List<MutableMap<String, Any?>>?, default: (List<T>)? = null) = list<T> {
    list?.let {
        list.withIndex().forEach { (index, map) -> put(createJsonData(map)).index = index }
    } ?: let {
        default?.let { putAll(default) }
    }
}