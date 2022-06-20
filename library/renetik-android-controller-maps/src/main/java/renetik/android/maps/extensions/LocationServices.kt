package renetik.android.maps.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

fun Context.locationClient(): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(this)

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.location(onSuccess: (Location) -> Unit) =
        lastLocation.addOnSuccessListener { location -> onSuccess(location) }