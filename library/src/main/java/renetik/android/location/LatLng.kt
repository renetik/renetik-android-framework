package renetik.android.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun LatLng.distanceTo(latLng: LatLng?) =
        latLng?.asLocation()?.distanceTo(asLocation()) ?: 0F

fun LatLng.asLocation() = Location("").also {
    it.latitude = latitude
    it.longitude = longitude
}

fun computeCenter(points: List<LatLng>): LatLng {
    var latitude = 0.0
    var longitude = 0.0
    val n = points.size
    for (point in points) {
        latitude += point.latitude
        longitude += point.longitude
    }
    return LatLng(latitude / n, longitude / n)
}