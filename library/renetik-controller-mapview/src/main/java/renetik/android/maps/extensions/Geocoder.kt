package renetik.android.maps.extensions

import android.location.Geocoder
import renetik.android.framework.lang.catchAllWarnReturnNull
import renetik.kotlin.collections.first

fun Geocoder.addressFromString(locationString: String) = catchAllWarnReturnNull {
    getFromLocationName(locationString, 1).first
}