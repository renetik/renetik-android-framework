package renetik.android.framework.json.data

import renetik.android.framework.event.event
import renetik.android.framework.event.fire
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

    constructor(data: CSStoreInterface) : this() {
        load(data)
    }

    constructor(data: String) : this() {
        load(data)
    }

    override val data = mutableMapOf<String, Any?>()
    var index: Int? = null
    override val eventLoaded = event()

    fun load(data: Map<String, Any?>) {
        this.data.putAll(data)
        eventLoaded.fire()
        onLoaded()
    }

    open fun onLoaded() {}

    override fun set(key: String, value: String?) = data.set(key, value)
    override fun set(key: String, value: Map<String, *>?) = data.set(key, value)
    override fun getMap(key: String) = data[key] as? MutableMap<String, Any?>
    override fun set(key: String, value: Array<*>?) = data.set(key, value)
    override fun getArray(key: String): Array<*>? = getList(key)?.toTypedArray()
    override fun set(key: String, value: List<*>?) = data.set(key, value)
    override fun getList(key: String): List<*>? = data[key] as? MutableList<Any?>

    override fun <T : CSJsonObject> getJsonList(key: String, type: KClass<T>): List<T>? {
        val isCreated = ((data[key] as? List<*>)?.at(0) as? T) != null
        return if (isCreated) data[key] as List<T> else
            (data[key] as? List<MutableMap<String, Any?>>)?.let { type.createJsonObjectList(it) }
    }

    override fun <T : CSJsonObject> set(key: String, jsonObject: T?) = data.set(key, jsonObject)
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

    override fun hashCode() = data.hashCode()
}



