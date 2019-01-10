package renetik.android.json.data.properties

import renetik.android.java.collections.CSMap
import renetik.android.java.collections.list
import renetik.android.java.common.CSSizeInterface
import renetik.android.java.extensions.collections.delete
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.lastItem
import renetik.android.json.createJsonDataList
import renetik.android.json.createJsonDataType
import renetik.android.json.data.CSJsonData
import java.io.File
import kotlin.reflect.KClass


class CSJsonString(val data: CSJsonData, private val key: String) : CharSequence {
    var string: String?
        get() = data.getString(key)
        set(value) = data.put(key, value)

    val value get() = string ?: ""
    override fun toString() = value
    override val length get() = value.length
    override fun get(index: Int) = value[index]
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)
}

class CSJsonBoolProperty(val data: CSJsonData, private val key: String) {
    var bool: Boolean?
        get() = data.getBoolean(key)
        set(value) = data.put(key, value)

    val value: Boolean get() = bool ?: false

    override fun toString() = "$value"
}

class CSJsonIntProperty(val data: CSJsonData, private val key: String) {
    var integer: Int?
        get() = data.getInt(key)
        set(value) = data.put(key, value)

    val value: Int get() = integer ?: 0

    override fun toString() = "$value"
}

class CSJsonFileProperty(val data: CSJsonData, private val key: String) {
    var file: File?
        get() = File(data.getString(key))
        set(file) = data.put(key, file?.toString())

    val value = file!!

    override fun toString() = "$file"
}

class CSJsonDataProperty<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                         private val key: String) {
    val value: T by lazy {
        createJsonDataType(type, data.getMap(key))
    }
}

@Suppress("unchecked_cast")
class CSJsonDataListProperty<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                             private val key: String) : Iterable<T>, CSSizeInterface {
    override fun iterator() = list.iterator()

    var list: List<T>
        get() = createJsonDataList(type, data.getList(key) as List<CSMap<String, Any?>>?)
        set(list) {
            data.getList(key)?.clear()
            list.forEach { item -> add(item) }
        }

    val last: T? get() = list.lastItem
    val isEmpty get() = size == 0
    override val size get() = data.getList(key)?.size ?: let { 0 }

    fun add(item: T): T {
        data.getList(key)?.add(item.getJsonDataMap())
                ?: data.put(key, list(item.getJsonDataMap()))
        return item
    }

    fun remove(item: T) = data.getList(key)?.delete(item.getJsonDataMap())
    fun removeLast() = data.getList(key)?.deleteLast()
    fun clear() = data.getList(key)?.clear()
    fun add(vararg items: T) = items.forEach { add(it) }
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