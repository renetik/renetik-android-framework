package renetik.android.maps.extensions

import android.location.Location
import com.google.android.gms.maps.model.PolylineOptions
import renetik.android.location.asLatLng

fun PolylineOptions.add(location: Location): PolylineOptions = add(location.asLatLng())