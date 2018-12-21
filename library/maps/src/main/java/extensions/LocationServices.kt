package renetik.android.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import renetik.android.controller.base.CSViewController

fun locationClient(controller: CSViewController<*>) = LocationServices.getFusedLocationProviderClient(controller.activity())

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.location(onSuccess: (Location) -> Unit) =
        lastLocation.addOnSuccessListener { location -> location?.let { onSuccess(it) } }