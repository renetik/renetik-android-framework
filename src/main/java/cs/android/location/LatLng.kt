package cs.android.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun LatLng.distanceTo(latLng: LatLng?) =
        latLng?.asLocation()?.distanceTo(asLocation()) ?: 0F

fun LatLng.asLocation() = Location("").also {
    it.latitude = latitude
    it.longitude = longitude
}
