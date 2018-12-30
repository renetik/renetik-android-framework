package renetik.android.json.data.properties

import renetik.android.java.collections.CSMap
import renetik.android.java.collections.list
import renetik.android.java.common.CSSizeInterface
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.lastItem
import renetik.android.json.createList
import renetik.android.json.data.CSJsonData
import java.io.File
import kotlin.reflect.KClass

class CSJsonStringProperty(val data: CSJsonData, private val key: String) {
    var string: String?
        get() = data.getString(key)
        set(value) = data.put(key, value)

    val value get() = string ?: ""

    override fun toString() = value
}

class CSJsonBoolProperty(val data: CSJsonData, private val key: String) {
    var bool: Boolean?
        get() = data.getBoolean(key)
        set(value) = data.put(key, value)

    val value: Boolean get() = bool ?: false

    override fun toString() = "$value"
}

class CSJsonFileProperty(val data: CSJsonData, private val key: String) {
    var value: File?
        get() = File(data.getString(key))
        set(file) = data.put(key, file?.toString())

    override fun toString() = "$value"
}

@Suppress("unchecked_cast")
class CSJsonDataListProperty<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                             val key: String) : Iterable<T>, CSSizeInterface {
    override fun iterator() = list.iterator()

    var list: List<T>
        get() = createList(type, data.getList(key) as List<CSMap<String, Any?>>?)
        set(list) {
            data.getList(key)?.clear()
            list.forEach { item -> add(item) }
        }

    val last: T? get() = list.lastItem
    val isEmpty get() = size == 0
    override val size get() = data.getList(key)?.size ?: let { 0 }

    fun add(item: T) = data.getList(key)?.add(item.getJsonDataMap())
            ?: data.put(key, list(item.getJsonDataMap()))

    fun remove(item: T) = data.getList(key)?.delete(item.getJsonDataMap())
    fun removeLast() = data.getList(key)?.deleteLast()
    fun clear() = data.getList(key)?.clear()
}

@Suppress("unchecked_cast")
class CSJsonStringListProperty(val data: CSJsonData, val key: String) : Iterable<String> {
    override fun iterator() = list.iterator()

    var list: List<String>
        get() = data.getList(key) as List<String>
        set(list) = data.put(key, list)

    val last: String? get() = list.lastItem
    val empty get() = size() == 0

    fun add(item: String) = data.getList(key)?.add(item)
            ?: data.put(key, list(item))

    fun remove(item: String) = data.getList(key)?.delete(item)
    fun removeLast() = data.getList(key)?.deleteLast()
    fun size() = data.getList(key)?.size ?: let { 0 }
}