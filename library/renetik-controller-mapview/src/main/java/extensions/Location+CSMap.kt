package renetik.android.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.asLatLng() = LatLng(latitude, longitude)
