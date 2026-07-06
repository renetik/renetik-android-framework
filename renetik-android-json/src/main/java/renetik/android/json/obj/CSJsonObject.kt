package renetik.android.json.obj

import renetik.android.core.lang.CSEnvironment.isTestRunner
import renetik.android.json.createJsonObject
import renetik.android.json.createJsonObjectList
import renetik.android.json.createJsonObjectMap
import renetik.android.json.toJsonType
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
open class CSJsonObject : CSJsonObjectInterface {
    open val data: MutableMap<String, Any?> =
        if (isTestRunner) sortedMapOf() else mutableMapOf()

    open fun onLoad() = Unit
    open fun onChange() = Unit

    open fun load(data: Map<String, Any?>) {
        this.data.putAll(data)
        onLoad()
        onChange()
    }

    open fun get(key: String): Any? = data[key]

    open fun save(key: String, value: Any?) {
        data[key] = value
        onChange()
    }

    override fun clear(key: String) {
        if (data.remove(key) == null) return
        onChange()
    }

    override fun clear() {
        if (data.isEmpty()) return
        data.clear()
        onLoad()
        onChange()
    }

    override val jsonMap: Map<String, *> get() = data

    override fun equals(other: Any?) =
        (other as? CSJsonObject)?.let { it.data == data } ?: super.equals(other)

    override fun hashCode() = data.hashCode()

    override fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>): T? =
        (get(key) as? MutableMap<String, Any?>)?.let(type::createJsonObject)

    override fun <T : CSJsonObject> setJsonObject(key: String, value: T?) {
        val map: Map<String, *>? = value?.jsonMap
        if (get(key) == map) return
        save(key, map?.toMutableMap())
    }

    override fun <T : CSJsonObject> getJsonObjectList(key: String, type: KClass<T>): List<T>? =
        (get(key) as? List<MutableMap<String, Any?>>)?.let { type.createJsonObjectList(it) }

    override fun <T : CSJsonObject> setJsonObjectList(key: String, list: List<T>?) {
        val value: List<MutableMap<String, Any?>>? =
            list?.let { List(list.size) { list[it].data } }
        if (get(key) == value) return
        save(key, value)
    }

    fun <T : CSJsonObject> getJsonObjectMap(key: String, type: KClass<T>): Map<String, T>? =
        (get(key) as? Map<String, MutableMap<String, Any?>>)?.let { type.createJsonObjectMap(it) }

    fun <T : CSJsonObject> setJsonObjectMap(key: String, map: Map<String, T>?) {
        val value: Map<String, Map<String, Any?>>? = map?.let {
            buildMap(map.size) { map.forEach { (key, value) -> this[key] = value.data } }
        }
        if (get(key) == value) return
        save(key, value)
    }

    private fun setValue(key: String, value: Any?) {
        val jsonValue = value.toJsonType()
        if (value != null && get(key) == jsonValue) return
        save(key, jsonValue)
    }

    override fun set(key: String, string: String?) = setValue(key, string)
    override fun set(key: String, boolean: Boolean?) = setValue(key, boolean)
    override fun set(key: String, int: Int?) = setValue(key, int)
    override fun set(key: String, long: Long?) = setValue(key, long)
    override fun set(key: String, float: Float?) = setValue(key, float)
    override fun set(key: String, double: Double?) = setValue(key, double)

    override fun set(key: String, value: List<*>?) {
        if (value != null && get(key) == value) return
        save(key, value?.toList())
    }

    override fun set(key: String, value: Map<String, *>?) {
        if (value != null && get(key) == value) return
        save(key, value?.toMap())
    }

}