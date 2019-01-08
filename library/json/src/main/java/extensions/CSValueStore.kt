package renetik.android.json.extensions

import renetik.android.base.CSValueStore
import renetik.android.java.collections.CSMap
import renetik.android.java.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.isEmpty
import renetik.android.json.createJsonDataList
import renetik.android.json.data.CSJsonData
import renetik.android.json.fromJson
import renetik.android.json.toJson
import kotlin.reflect.KClass

fun CSValueStore.put(key: String, value: Any?) = value?.let {
    val editor = preferences.edit()
    editor.putString(key, toJson(it))
    editor.apply()
} ?: clear(key)

fun <T : CSJsonData> CSValueStore.load(data: T, key: String): T? {
    val loadString = loadString(key) ?: return null
    fromJson<CSMap<String, Any?>>(loadString)?.let { data.load(it) }
    return data
}

fun <T : CSJsonData> CSValueStore.loadDataList(type: KClass<T>, key: String) = createJsonDataList(type, loadJson(key))

fun <T : CSJsonData> CSValueStore.loadDataList(type: KClass<T>, key: String, default: List<T>) =
        createJsonDataList(type, loadJson(key), default)

fun <T : Any> CSValueStore.loadList(key: String) =
        list<T>().apply { loadJson<List<T>>(key)?.forEach { data -> put(data) } }

private fun <Type> CSValueStore.loadJson(key: String): Type? {
    val loadString = loadString(key)
    return if (loadString.isEmpty) null else fromJson<Type>(loadString!!)
}
