package renetik.android.json.extensions

import renetik.android.framework.CSValueStoreInterface
import renetik.android.java.extensions.collections.list
import renetik.android.json.data.CSJsonMap
import renetik.android.json.parseJson
import renetik.android.json.toJsonString
import renetik.android.primitives.isEmpty
import kotlin.reflect.KClass

fun CSValueStoreInterface.save(key: String, value: Any?) =
    save(key, value?.toJsonString())

fun <T : CSJsonMap> CSValueStoreInterface.load(data: T, key: String): T? {
    val loadString = getString(key) ?: return null
    loadString.parseJson<MutableMap<String, Any?>>()?.let { data.load(it) }
    return data
}

fun <T : CSJsonMap> CSValueStoreInterface.load(
    type: KClass<T>, key: String): T = type.createJsonMap(loadJson(key))

fun <T : CSJsonMap> CSValueStoreInterface.loadList(
    type: KClass<T>, key: String): MutableList<T> =
    type.createJsonDataList(loadJson(key))

fun <T : CSJsonMap> CSValueStoreInterface.loadList(
    type: KClass<T>, key: String, default: List<T>): MutableList<T> =
    type.createJsonDataList(loadJson(key), default)

fun <T : Any> CSValueStoreInterface.loadList(key: String): MutableList<T> =
    loadJson<List<T>>(key) as? MutableList<T> ?: list()

private fun <Type> CSValueStoreInterface.loadJson(key: String): Type? {
    val loadString = getString(key)
    return if (loadString.isEmpty) null else loadString!!.parseJson<Type>()
}
