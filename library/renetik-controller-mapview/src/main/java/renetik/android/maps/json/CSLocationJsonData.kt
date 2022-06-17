package renetik.android.maps.json

import com.google.android.gms.maps.model.LatLng
import renetik.android.store.json.CSStoreJsonObject

class CSLocationJsonData() : CSStoreJsonObject() {

    val latLng get() = locationProperty.latLng!!

    constructor(latLng: LatLng) : this() {
        locationProperty.latLng = latLng
    }

    private val locationProperty = CSJsonLocation(this, "location")
}