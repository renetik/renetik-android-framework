package renetik.android.sample.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.view.View
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.extensions.title
import renetik.android.location.asLatLng
import renetik.android.maps.CSMapClientController
import renetik.android.maps.CSMapController
import renetik.android.maps.extensions.add
import renetik.android.sample.R

@SuppressLint("MissingPermission")
class SampleMapPathController(title: String, private val mapController: CSMapController) : CSViewController<View>(navigation, layout(R.layout.sample_map)),
        CSNavigationItem {

    private val mapClient = CSMapClientController(this, R.id.SampleMap_Map, mapController)
    private var previousPosition: Location? = null

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            previousPosition?.let { mapController.map?.addPolyline(lineOptions().add(it).add(result.lastLocation)) }
            previousPosition = result.lastLocation
            mapController.camera(result.lastLocation.asLatLng())
        }
    }

    private val lineOptions = {
        PolylineOptions().jointType(JointType.ROUND)
                .startCap(RoundCap()).endCap(RoundCap()).width(12F).color(Color.RED)
    }

    init {
        title(R.id.SampleMap_Title, title)
        mapClient.onMapShowing { map ->
            map.isMyLocationEnabled = true
            getFusedLocationProviderClient(this).requestLocationUpdates(LocationRequest(), locationCallback, null)
        }
    }

    override fun onViewHiding() {
        super.onViewHiding()
        getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
    }
}