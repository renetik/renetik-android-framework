package renetik.android.maps.extensions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri.parse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import renetik.android.controller.base.CSActivityView
import renetik.android.framework.common.catchError

fun CSActivityView<*>.locationClient(): FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(activity())

fun <T : CSActivityView<*>> T.navigateToLatLng(latLng: LatLng, title: String) {
    val uri = "http://maps.google.com/maps?&daddr=${latLng.latitude},${latLng.longitude} (${title})"
    try {
        startActivity(Intent(ACTION_VIEW, parse(uri)).apply {
            setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        })
    } catch (ex: ActivityNotFoundException) {
        catchError<ActivityNotFoundException> {
            startActivity(Intent(ACTION_VIEW, parse(uri)))
        }
    }
}