package renetik.android.framework.json.data

import renetik.android.framework.json.extensions.createJsonObject
import renetik.android.framework.json.extensions.createJsonObjectList
import renetik.android.framework.json.toJsonString
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.collections.at
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
open class CSJsonObject() : Iterable<Map.Entry<String, Any?>>, CSStoreInterface {

    constructor(map: MutableMap<String, Any?>) : this() {
        load(map)
    }

    override val data = mutableMapOf<String, Any?>()

    var index: Int? = null
    var isLoaded = false

    fun load(data: Map<String, Any?>) {
        this.data.putAll(data)
        if (!isLoaded) isLoaded = true
    }

    override fun save(key: String, value: String?) = data.set(key, value)
    override fun save(key: String, value: Map<String, *>?) = data.set(key, value)
    override fun getMap(key: String) = data[key] as? MutableMap<String, Any?>
    override fun save(key: String, value: Array<*>?) = data.set(key, value)
    override fun getArray(key: String): Array<*>? = getList(key)?.toTypedArray()
    override fun save(key: String, value: List<*>?) = data.set(key, value)
    override fun getList(key: String): List<*>? = data[key] as? MutableList<Any?>

    override fun <T : CSJsonObject> getJsonList(key: String, type: KClass<T>): List<T>? {
        val isCreated = ((data[key] as? List<*>)?.at(0) as? T) != null
        return if (isCreated) data[key] as List<T> else
            (data[key] as? List<MutableMap<String, Any?>>)?.let { type.createJsonObjectList(it) }
    }

    override fun <T : CSJsonObject> save(key: String, jsonObject: T?) = data.set(key, jsonObject)
    override fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>): T? {
        return data[key] as? T
            ?: (data[key] as? MutableMap<String, Any?>)?.let { type.createJsonObject(it) }
    }

    override fun load(store: CSStoreInterface) = load(store.data)

    override fun clear() = data.clear()

    override fun clear(key: String) {
        data.remove(key)
    }

    override fun toString() = toJsonString(formatted = true)

    override fun equals(other: Any?) =
        (other as? CSJsonObject)?.let { it.data == data } ?: super.equals(other)
}



