package cs.android.json

import com.google.android.gms.maps.model.LatLng

class CSLocationJsonData() : CSJsonData() {

    val latLng get() = locationProperty.latLng!!

    constructor(latLng: LatLng) : this() {
        locationProperty.latLng = latLng
    }

    private val locationProperty = CSJsonLocationProperty(this, "location")
}