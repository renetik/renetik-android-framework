package cs.android.json

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import cs.java.collections.CSMap
import cs.java.lang.CSLang.list
import java.io.File
import kotlin.reflect.KClass

class CSJsonStringProperty(val data: CSJsonData, private val key: String) {
    var value: String?
        get() = data.getString(key)
        set(value) = data.put(key, value)
}

class CSJsonFileProperty(val data: CSJsonData, private val key: String) {
    var value: File?
        get() = File(data.getString(key))
        set(file) = data.put(key, file?.toString())
}

class CSJsonLocationProperty(val data: CSJsonData, private val key: String) {
    var value: LatLng?
        get() {
            return (data.getList(key) as? List<Double>)?.let { LatLng(it[0], it[1]) }
        }
        set(latLng) = data.put(key, list(latLng?.latitude, latLng?.longitude))

    fun set(location: Location) {
        value = LatLng(location.latitude, location.longitude)
    }
}

class CSJsonGetStringProperty(var data: CSJsonData, private var key: String) {
    val value: String? get() = data.getString(key)
}

class CSJsonListProperty<T : CSJsonData>(val data: CSJsonData, val type: KClass<T>,
                                         val key: String) {
    val value: List<T>
        get() = createList(type.java, data.getList(key) as List<CSMap<String, Any?>>?)

    fun add(item: T) {
        data.getList(key)?.add(item.getJsonDataMap()) ?: data.put(key, list(item.getJsonDataMap()))
    }
}

class CSJsonFileListProperty(val data: CSJsonData, private val key: String) {
    var value: List<File>
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

    fun add(file: File) {
        data.getList(key)?.add(file.toString()) ?: data.put(key, list(file.toString()))
    }
}

class CSJsonLocationListProperty(val data: CSJsonData, private val key: String) {
    var value: List<LatLng>
        get() {
            val list = list<LatLng>()
            data.getList(key)?.forEach { type: Any? ->
                run {
                    val latLngString = type.toString().split(":")
                    list.add(LatLng(latLngString[0].toDouble(), latLngString[1].toDouble()))
                }
            }
            return list
        }
        set(locationList) {
            val stringList = list<String>()
            locationList.forEach { location: LatLng -> stringList.add(asString(location)) }
            data.put(key, stringList)
        }

    fun add(location: LatLng) {
        data.getList(key)?.add(asString(location)) ?: data.put(key, list(asString(location)))
    }

    fun add(location: Location) {
        data.getList(key)?.add(asString(location)) ?: data.put(key, list(asString(location)))
    }

    private fun asString(latLng: Location) = "${latLng.latitude}:${latLng.longitude}"
    private fun asString(latLng: LatLng) = "${latLng.latitude}:${latLng.longitude}"
}