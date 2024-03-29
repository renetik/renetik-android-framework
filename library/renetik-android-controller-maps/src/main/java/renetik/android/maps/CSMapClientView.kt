package renetik.android.maps

import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import renetik.android.controller.base.CSActivityView
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.listen
import renetik.android.ui.extensions.frame
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.removeFromSuperview
import renetik.android.ui.extensions.widget.layoutMatch
import renetik.android.ui.protocol.registerUntilHide

open class CSMapClientView<V : View>(parent: CSActivityView<V>, private val mapFrameId: Int,
                                     open val mapController: CSMapView)
    : CSActivityView<V>(parent) {

    private var lastLocation: LatLng? = null
    private var lastZoom: Float? = null
    private val onMapShowingEvent = event<GoogleMap>()
    fun onMapShowing(function: (GoogleMap) -> Unit) =
        onMapShowingEvent.listen { _, map -> function(map) }

    private val onMapClickEvent = event<LatLng>()
    fun onMapClick(function: (LatLng) -> Unit) =
        onMapClickEvent.listen { _, location -> function(location) }

    private val onMapLongClickEvent = event<LatLng>()
    val map get() = mapController.map

    fun onMapLongClick(function: (LatLng) -> Unit) =
        onMapLongClickEvent.listen { _, location -> function(location) }

    @SuppressLint("MissingPermission")
    override fun onViewShowing() {
        super.onViewShowing()
        registerUntilHide(mapController.onMapAvailable { map ->
            frame(mapFrameId).add(mapController.view.removeFromSuperview(), layoutMatch)
            map.clear()
            map.setOnMapClickListener { latLng -> onMapClickEvent.fire(latLng) }
            map.setOnMapLongClickListener { latLng -> onMapLongClickEvent.fire(latLng) }
            onMapShowingEvent.fire(map)
        })
        registerUntilHide(mapController.onCameraStopped { map ->
            lastLocation = map.cameraPosition.target
            lastZoom = map.cameraPosition.zoom
        })
        lastLocation?.let { latLng -> mapController.camera(latLng, lastZoom!!) }
    }

}


