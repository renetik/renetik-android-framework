package renetik.android.maps.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.asLatLng() = LatLng(latitude, longitude)
