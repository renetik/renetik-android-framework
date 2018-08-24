package cs.android.json

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import cs.java.collections.CSList
import cs.java.collections.CSMap
import cs.java.lang.CSLang.list
import java.io.File
import kotlin.reflect.KClass

class CSJsonStringProperty(val data: CSJsonData, private val key: String) {
    var string: String?
        get() = data.getString(key)
        set(value) = data.put(key, value)

    override fun toString(): String {
        return string ?: super.toString()
    }
}

class CSJsonBoolProperty(val data: CSJsonData, private val key: String) {
    var bool: Boolean?
        get() = data.getBoolean(key)
        set(value) = data.put(key, value)
    val isTrue: Boolean get() = bool ?: false
}

class CSJsonFileProperty(val data: CSJsonData, private val key: String) {
    var value: File?
        get() = File(data.getString(key))
        set(file) = data.put(key, file?.toString())
}

@Suppress("unchecked_cast")
class CSJsonLocationProperty(val data: CSJsonData, private val key: String) {
    var latLng: LatLng?
        get() = (data.getList(key) as? List<Double>)?.let { LatLng(it[0], it[1]) }
        set(latLng) = data.put(key, list(latLng?.latitude, latLng?.longitude))

    fun set(location: Location) {
        latLng = LatLng(location.latitude, location.longitude)
    }
}

class CSJsonGetStringProperty(var data: CSJsonData, private var key: String) {
    val value: String? get() = data.getString(key)
}

@Suppress("unchecked_cast")
class CSJsonListProperty<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                         val key: String) {
    val list: CSList<T>
        get() = createList(type.java, data.getList(key) as List<CSMap<String, Any?>>?)

    val last: T? get() = list.last()

    fun add(item: T) =
            data.getList(key)?.add(item.getJsonDataMap())
                    ?: data.put(key, list(item.getJsonDataMap()))

    fun remove(item: T) =
            data.getList(key)?.remove(item.getJsonDataMap())
}

class CSJsonFileListProperty(val data: CSJsonData, private val key: String) {
    var list: CSList<File>
        get() {
            val list = list<File>()
            data.getList(key)?.forEach { type: Any? -> list.add(File(type.toString())) }
            return list
        }
        set(fileList) {
            val stringList = list<String>()
            fileList.forEach { file: File -> stringList.add(file.toString()) }
            data.put(key, stringList)
        }

    val last: File? get() = list.last()

    fun add(file: File) =
            data.getList(key)?.add(file.toString()) ?: data.put(key, list(file.toString()))
}

class CSJsonLocationListProperty(val data: CSJsonData, private val key: String) {
    var list: CSList<LatLng>
        get() = list<LatLng>().apply {
            data.getList(key)?.forEach { type ->
                type.toString().split(":").also { add(LatLng(it[0].toDouble(), it[1].toDouble())) }
            }
        }
        set(locationList) = data.put(key, list<String>()
                .apply { locationList.forEach { location -> add(asString(location)) } })

    val last: LatLng? get() = list.last()

    fun add(location: LatLng) =
            data.getList(key)?.add(asString(location)) ?: data.put(key, list(asString(location)))

    fun add(location: Location) =
            data.getList(key)?.add(asString(location)) ?: data.put(key, list(asString(location)))

    private fun asString(latLng: Location) = "${latLng.latitude}:${latLng.longitude}"
    private fun asString(latLng: LatLng) = "${latLng.latitude}:${latLng.longitude}"
}