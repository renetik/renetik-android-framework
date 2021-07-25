package renetik.android.json.store

import renetik.android.framework.store.CSStoreInterface
import renetik.android.java.extensions.collections.list
import renetik.android.json.data.CSJsonMap
import renetik.android.json.extensions.createJsonDataList
import renetik.android.json.extensions.createJsonMap
import renetik.android.json.parseJson
import renetik.android.json.toJsonString
import renetik.android.primitives.isEmpty
import kotlin.reflect.KClass

fun CSStoreInterface.save(key: String, value: Any?) =
    save(key, value?.toJsonString())

fun <T : CSJsonMap> CSStoreInterface.load(data: T, key: String): T? {
    val loadString = getString(key) ?: return null
    loadString.parseJson<MutableMap<String, Any?>>()?.let { data.load(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun <T : CSJsonMap> CSStoreInterface.load(
    type: KClass<T>, key: String, default: T?
): T? {
    val data = loadJson<Any>(key) as? Map<String, Any?> ?: return default
    return type.createJsonMap(data)
}

@Suppress("UNCHECKED_CAST")
fun <T : CSJsonMap> CSStoreInterface.load(
    type: KClass<T>, key: String
): T = type.createJsonMap(loadJson(key))

fun <T : CSJsonMap> CSStoreInterface.loadList(
    type: KClass<T>, key: String
): MutableList<T> =
    type.createJsonDataList(loadJson(key))

fun <T : CSJsonMap> CSStoreInterface.loadList(
    type: KClass<T>, key: String, default: List<T>
): MutableList<T> =
    type.createJsonDataList(loadJson(key), default)

fun <T : Any> CSStoreInterface.loadList(key: String): MutableList<T> =
    loadJson<List<T>>(key) as? MutableList<T> ?: list()

private fun <Type> CSStoreInterface.loadJson(key: String): Type? {
    val loadString = getString(key)
    return if (loadString.isEmpty) null else loadString!!.parseJson<Type>()
}

fun <T : CSJsonMap> CSStoreInterface.property(
    key: String, listType: KClass<T>, default: List<T>, onApply: ((value: List<T>) -> Unit)? = null
) = CSListStoreEventProperty(this, key, listType, default, onApply)

fun <T : CSJsonMap> CSStoreInterface.property(
    key: String, type: KClass<T>, default: T? = null, onApply: ((value: T?) -> Unit)? = null
) = CSItemStoreEventProperty(this, key, type, default, onApply)

