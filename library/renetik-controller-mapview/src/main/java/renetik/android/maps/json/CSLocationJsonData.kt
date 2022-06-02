package renetik.android.maps.json

import com.google.android.gms.maps.model.LatLng
import renetik.android.framework.json.CSJsonObject

class CSLocationJsonData() : CSJsonObject() {

    val latLng get() = locationProperty.latLng!!

    constructor(latLng: LatLng) : this() {
        locationProperty.latLng = latLng
    }

    private val locationProperty = CSJsonLocation(this, "location")
}