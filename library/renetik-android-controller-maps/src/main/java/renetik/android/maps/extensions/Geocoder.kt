package renetik.android.maps.extensions

import android.location.Geocoder
import renetik.android.core.kotlin.collections.first
import renetik.android.core.lang.catchAllWarnReturnNull

@Suppress("DEPRECATION")
fun Geocoder.addressFromString(locationString: String) = catchAllWarnReturnNull {
    getFromLocationName(locationString, 1)?.first
}