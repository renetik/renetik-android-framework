package renetik.android.framework.json

import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.store.CSStore
import renetik.android.primitives.toArray
import renetik.kotlin.collections.at
import java.io.Closeable
import kotlin.reflect.KClass

@Suppress("unchecked_cast")
open class CSJsonObject() : Iterable<Map.Entry<String, Any?>>, CSStore, Closeable {

    constructor(map: MutableMap<String, Any?>) : this() {
        load(map)
    }

    constructor(data: CSStore) : this() {
        load(data)
    }

    constructor(data: String) : this() {
        load(data)
    }

    override val data = mutableMapOf<String, Any?>()
    override val eventChanged = event<CSStore>()
    final override fun load(store: CSStore) = load(store.data)

    fun load(data: Map<String, Any?>) {
        this.data.putAll(data)
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
        onLoaded()
    }

    open fun onLoaded() = Unit

    open fun onChanged() = eventChanged.fire(this)

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

    override fun set(key: String, value: String?) {
        if (value != null && data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun set(key: String, value: Map<String, *>?) {
        if (value != null && data[key] == value) return
        data[key] = value?.toMap()
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun getMap(key: String) = data[key] as? MutableMap<String, Any?>

    override fun set(key: String, value: Array<*>?) {
        if (value != null && data[key] == value) return
        data[key] = value?.toArray()
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun getArray(key: String): Array<*>? = getList(key)?.toTypedArray()

    override fun set(key: String, value: List<*>?) {
        if (value != null && data[key] == value) return
        data[key] = value?.toList()
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun getList(key: String): List<*>? = data[key] as? MutableList<Any?>

    override fun <T : CSJsonObject> getJsonObjectList(key: String, type: KClass<T>): List<T>? {
        val isFirstItemJsonObject = ((data[key] as? List<*>)?.at(0) as? T) != null
        return if (isFirstItemJsonObject) data[key] as List<T> else
            (data[key] as? List<MutableMap<String, Any?>>)?.let { list ->
                type.createJsonObjectList(list).also { data[key] = it }
            }
    }

    override fun <T : CSJsonObject> set(key: String, value: T?) {
        if (value != null && data[key] == value) return
        data[key] = value
        if (!isBulkSave) onChanged()
        else isBulkSaveDirty = true
    }

    override fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>): T? =
        data[key] as? T ?: (data[key] as? MutableMap<String, Any?>)?.let { map ->
            type.createJsonObject(map).also { data[key] = it }
        }

    override fun toString() = super.toString() + toJsonString(formatted = true)

    override fun equals(other: Any?) =
        (other as? CSJsonObject)?.let { it.data == data } ?: super.equals(other)

    override fun hashCode() = data.hashCode()

    protected var isBulkSave = false
    private var isBulkSaveDirty = false

    override fun bulkSave() = apply {
        isBulkSave = true
        return this
    }

    override fun close() {
        if (isBulkSaveDirty) onChanged()
        isBulkSaveDirty = false
        isBulkSave = false
    }
}



