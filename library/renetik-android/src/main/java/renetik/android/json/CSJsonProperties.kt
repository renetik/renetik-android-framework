package renetik.android.json

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import renetik.android.location.asLatLng
import renetik.java.collections.CSList
import renetik.java.collections.CSMap
import renetik.java.collections.list
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
class CSJsonLocationProperty(val data: CSJsonData, private val key: String) {
    var latLng: LatLng?
        get() = (data.getList(key) as? List<Double>)?.let { LatLng(it[0], it[1]) }
        set(latLng) = data.put(key, list(latLng?.latitude, latLng?.longitude))

    fun set(location: Location) {
        latLng = location.asLatLng()
    }

    override fun toString() = "$latLng"
}

@Suppress("unchecked_cast")
class CSJsonDataListProperty<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                             val key: String) : Iterable<T> {
    override fun iterator() = list.iterator()

    var list: CSList<T>
        get() = createList(type, data.getList(key) as List<CSMap<String, Any?>>?)
        set(list) = list.forEach { item -> add(item) }

    val last: T? get() = list.last()
    val empty get() = size() == 0

    fun add(item: T) = data.getList(key)?.add(item.getJsonDataMap())
            ?: data.put(key, list(item.getJsonDataMap()))

    fun remove(item: T) = data.getList(key)?.delete(item.getJsonDataMap())
    fun removeLast() = data.getList(key)?.deleteLast()
    fun size() = data.getList(key)?.size ?: let { 0 }
}

@Suppress("unchecked_cast")
class CSJsonStringListProperty(val data: CSJsonData, val key: String) : Iterable<String> {
    override fun iterator() = list.iterator()

    var list: CSList<String>
        get() = data.getList(key) as CSList<String>
        set(list) = data.put(key, list)

    val last: String? get() = list.last()
    val empty get() = size() == 0

    fun add(item: String) = data.getList(key)?.add(item)
            ?: data.put(key, list(item))

    fun remove(item: String) = data.getList(key)?.delete(item)
    fun removeLast() = data.getList(key)?.deleteLast()
    fun size() = data.getList(key)?.size ?: let { 0 }
}