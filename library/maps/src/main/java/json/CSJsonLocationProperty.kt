package renetik.android.maps.json

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import renetik.android.json.data.CSJsonData
import renetik.android.location.asLatLng
import renetik.android.java.collections.list

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