package renetik.android.json

import renetik.android.core.kotlin.collections.at
import renetik.android.core.kotlin.primitives.toArray
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
open class CSJsonObject() : Iterable<Map.Entry<String, Any?>> {

    open val data = mutableMapOf<String, Any?>()

    constructor(map: MutableMap<String, Any?>) : this() {
        data.putAll(map)
    }

    constructor(data: String) : this() {
        load(data)
    }

    open fun load(data: Map<String, Any?>) = this.data.putAll(data)

    open fun clear() = data.clear()

    open fun clear(key: String) {
        data.remove(key)
    }

    open fun set(key: String, value: String?) {
        if (value != null && data[key] == value) return
        data[key] = value
    }

    open fun set(key: String, value: Map<String, *>?) {
        if (value != null && data[key] == value) return
        data[key] = value?.toMap()
    }

    open fun getMap(key: String) = data[key] as? MutableMap<String, Any?>

    open fun set(key: String, value: Array<*>?) {
        if (value != null && data[key] == value) return
        data[key] = value?.toArray()
    }

    open fun getArray(key: String): Array<*>? = getList(key)?.toTypedArray()

    open fun set(key: String, value: List<*>?) {
        if (value != null && data[key] == value) return
        data[key] = value?.toList()
    }

    open fun getList(key: String): List<*>? = data[key] as? MutableList<Any?>

    open fun <T : CSJsonObject> getJsonObjectList(key: String, type: KClass<T>): List<T>? {
        val isFirstItemJsonObject = ((data[key] as? List<*>)?.at(0) as? T) != null
        return if (isFirstItemJsonObject) data[key] as List<T> else
            (data[key] as? List<MutableMap<String, Any?>>)?.let { list ->
                type.createJsonObjectList(list).also { data[key] = it }
            }
    }

    open fun <T : CSJsonObject> set(key: String, value: T?) {
        if (value != null && data[key] == value) return
        data[key] = value
    }

    open fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>): T? =
        data[key] as? T ?: (data[key] as? MutableMap<String, Any?>)?.let { map ->
            type.createJsonObject(map).also { data[key] = it }
        }

    override fun toString() = super.toString() + toJsonString(formatted = true)

    override fun equals(other: Any?) =
        (other as? CSJsonObject)?.let { it.data == data } ?: super.equals(other)

    override fun hashCode() = data.hashCode()

    override fun iterator(): Iterator<Map.Entry<String, Any?>> = data.iterator()
}