package renetik.android.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun LatLng.distanceTo(latLng: LatLng?) =
        latLng?.asLocation()?.distanceTo(asLocation()) ?: 0F

fun LatLng.asLocation() = Location("").also {
    it.latitude = latitude
    it.longitude = longitude
}

fun List<LatLng>.computeCenter(): LatLng {
    var latitude = 0.0
    var longitude = 0.0
    val n = size
    for (point in this) {
        latitude += point.latitude
        longitude += point.longitude
    }
    return LatLng(latitude / n, longitude / n)
}