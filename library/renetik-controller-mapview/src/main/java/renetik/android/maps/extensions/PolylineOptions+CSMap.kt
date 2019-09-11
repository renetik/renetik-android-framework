package renetik.android.maps.extensions

import android.location.Location
import com.google.android.gms.maps.model.PolylineOptions

fun PolylineOptions.add(location: Location): PolylineOptions = add(location.asLatLng())