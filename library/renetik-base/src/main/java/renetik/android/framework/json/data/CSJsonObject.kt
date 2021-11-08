package renetik.android.framework.json.data

import renetik.android.framework.event.event
import renetik.android.framework.json.extensions.createJsonObject
import renetik.android.framework.json.extensions.createJsonObjectList
import renetik.android.framework.json.toJsonString
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.collections.at
import java.io.Closeable
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
open class CSJsonObject() : Iterable<Map.Entry<String, Any?>>, CSStoreInterface, Closeable {

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
    override val eventChanged = event<CSStoreInterface>()

    override fun load(store: CSStoreInterface) = load(store.data)

    fun load(data: Map<String, Any?>) {
        this.data.putAll(data)
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    open fun onChanged() {
        eventChanged.fire(this)
    }

    override fun set(key: String, value: String?) {
        if (data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun set(key: String, value: Map<String, *>?) {
        if (data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun set(key: String, value: Array<*>?) {
        if (data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun set(key: String, value: List<*>?) {
        if (data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun <T : CSJsonObject> set(key: String, value: T?) {
        if (data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun clear() {
        data.clear()
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun clear(key: String) {
        if (data.remove(key) == null) return
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    protected var isBulkSave = false
    protected var isBulkSaveDirty = false

    override fun bulkSave() = apply {
        isBulkSave = true
        return this
    }

    override fun close() {
        isBulkSave = false
        if (isBulkSaveDirty) onChanged()
        isBulkSaveDirty = false
    }

    override fun getMap(key: String) = data[key] as? MutableMap<String, Any?>
    override fun getArray(key: String): Array<*>? = getList(key)?.toTypedArray()
    override fun getList(key: String): List<*>? = data[key] as? MutableList<Any?>
    override fun <T : CSJsonObject> getJsonList(key: String, type: KClass<T>): List<T>? {
        val isCreated = ((data[key] as? List<*>)?.at(0) as? T) != null
        return if (isCreated) data[key] as List<T> else
            (data[key] as? List<MutableMap<String, Any?>>)?.let { type.createJsonObjectList(it) }
    }

    override fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>): T? {
        return data[key] as? T
            ?: (data[key] as? MutableMap<String, Any?>)?.let { type.createJsonObject(it) }
    }

    override fun toString() = super.toString() + toJsonString(formatted = true)

    override fun equals(other: Any?) =
        (other as? CSJsonObject)?.let { it.data == data } ?: super.equals(other)

    override fun hashCode() = data.hashCode()
}



