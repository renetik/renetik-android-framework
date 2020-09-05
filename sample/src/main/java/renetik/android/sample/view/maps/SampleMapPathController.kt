package renetik.android.sample.view.maps

import android.annotation.SuppressLint
import android.graphics.Color.RED
import android.view.View
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.JointType.ROUND
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.extensions.textView
import renetik.android.java.common.CSTimeConstants.Second
import renetik.android.java.extensions.collections.beforeLast
import renetik.android.maps.CSMapClientController
import renetik.android.maps.CSMapController
import renetik.android.maps.extensions.asLatLng
import renetik.android.sample.R
import renetik.android.sample.model.MapPosition
import renetik.android.sample.model.model
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.text

@SuppressLint("MissingPermission")
class SampleMapPathController(title: String, private val mapController: CSMapController)
    : CSViewController<View>(navigation, layout(R.layout.sample_map_path)) {

    private val mapClient = CSMapClientController(this, R.id.SampleMap_Map, mapController)
    private val locationRequest = LocationRequest().apply {
        priority = PRIORITY_HIGH_ACCURACY
        interval = 2L * Second
        fastestInterval = 2L * Second
        smallestDisplacement = 5F
    }
    private val lineOptions
        get() = PolylineOptions().jointType(ROUND)
            .startCap(RoundCap()).endCap(RoundCap()).width(12F).color(RED)

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            model.mapRoute.add(MapPosition(result.lastLocation.asLatLng()))
            model.save()
            model.mapRoute.list.beforeLast?.let {
                mapController.map?.addPolyline(lineOptions.add(it.latLng)
                    .add(model.mapRoute.last!!.latLng))
            }
            mapController.camera(result.lastLocation, 11f)
        }
    }

    init {
        textView(R.id.SampleMap_Title).text(title)
        mapClient.onMapShowing { map ->
            map.isMyLocationEnabled = true
            map.addPolyline(lineOptions.apply { model.mapRoute.forEach { add(it.latLng) } })
            getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest,
                locationCallback,
                null)
        }
    }

    override fun onViewHiding() {
        super.onViewHiding()
        getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
    }
}