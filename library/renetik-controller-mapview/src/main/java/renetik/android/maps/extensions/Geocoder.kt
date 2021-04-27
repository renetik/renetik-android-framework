package renetik.android.maps.extensions

import android.location.Geocoder
import renetik.android.framework.common.catchAllWarnReturnNull
import renetik.android.java.extensions.collections.first

fun Geocoder.addressFromString(locationString: String) = catchAllWarnReturnNull {
    getFromLocationName(locationString, 1).first
}