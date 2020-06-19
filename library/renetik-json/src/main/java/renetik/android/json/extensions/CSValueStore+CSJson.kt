package renetik.android.json.extensions

import renetik.android.base.CSValueStore
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.isEmpty
import renetik.android.json.data.CSJsonData
import renetik.android.json.parseJson
import renetik.android.json.toJsonString
import kotlin.reflect.KClass

fun CSValueStore.save(key: String, value: Any?) = value?.let {
    val editor = preferences.edit()
    editor.putString(key, it.toJsonString())
    editor.apply()
} ?: clear(key)

fun <T : CSJsonData> CSValueStore.load(data: T, key: String): T? {
    val loadString = getString(key) ?: return null
    loadString.parseJson<MutableMap<String, Any?>>()?.let { data.load(it) }
    return data
}

fun <T : CSJsonData> CSValueStore.load(type: KClass<T>, key: String) =
        type.createJsonData(loadJson(key))

fun <T : CSJsonData> CSValueStore.loadList(type: KClass<T>, key: String) =
        type.createJsonDataList(loadJson(key))

fun <T : CSJsonData> CSValueStore.loadList(type: KClass<T>, key: String, default: List<T>) =
        type.createJsonDataList(loadJson(key), default)

fun <T : Any> CSValueStore.loadList(key: String) =
        list<T>().apply { loadJson<List<T>>(key)?.forEach { data -> put(data) } }

private fun <Type> CSValueStore.loadJson(key: String): Type? {
    val loadString = getString(key)
    return if (loadString.isEmpty) null else loadString!!.parseJson<Type>()
}
