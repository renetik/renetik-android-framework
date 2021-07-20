package renetik.android.maps

import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.extensions.frame
import renetik.android.java.event.event
import renetik.android.view.extensions.add
import renetik.android.view.extensions.layoutMatch
import renetik.android.view.extensions.removeFromSuperview

open class CSMapClientView<V : View>(parent: CSActivityView<V>, private val mapFrameId: Int,
                                     open val mapController: CSMapView)
    : CSActivityView<V>(parent) {

    private var lastLocation: LatLng? = null
    private var lastZoom: Float? = null
    private val onMapShowingEvent = event<GoogleMap>()
    fun onMapShowing(function: (GoogleMap) -> Unit) =
        onMapShowingEvent.add { _, map -> function(map) }

    private val onMapClickEvent = event<LatLng>()
    fun onMapClick(function: (LatLng) -> Unit) =
        onMapClickEvent.add { _, location -> function(location) }

    private val onMapLongClickEvent = event<LatLng>()
    val map get() = mapController.map

    fun onMapLongClick(function: (LatLng) -> Unit) =
        onMapLongClickEvent.add { _, location -> function(location) }


    @SuppressLint("MissingPermission")
    override fun onViewShowing() {
        super.onViewShowing()
        mapController.onMapAvailable(this) { map ->
            frame(mapFrameId).add(mapController.view.removeFromSuperview(), layoutMatch)
            map.clear()
            map.setOnMapClickListener { latLng -> onMapClickEvent.fire(latLng) }
            map.setOnMapLongClickListener { latLng -> onMapLongClickEvent.fire(latLng) }
            onMapShowingEvent.fire(map)
        }
        whileShowing(mapController.onCameraStopped { map ->
            lastLocation = map.cameraPosition.target
            lastZoom = map.cameraPosition.zoom
        })
        lastLocation?.let { latLng -> mapController.camera(latLng, lastZoom!!) }
    }

}


