package renetik.android.maps.extensions

import android.location.Geocoder
import renetik.android.java.common.tryAndCatch
import renetik.android.java.extensions.collections.first

fun Geocoder.addressFromString(locationString: String) =
        tryAndCatch({ getFromLocationName(locationString, 1).first }, onException = { null })