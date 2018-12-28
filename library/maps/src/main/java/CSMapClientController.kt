package renetik.android.maps

import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import renetik.android.controller.base.CSViewController
import renetik.android.extensions.frame
import renetik.android.java.event.CSEvent
import renetik.android.java.event.event
import renetik.android.view.extensions.add
import renetik.android.view.extensions.layoutMatch
import renetik.android.view.extensions.removeFromSuperview

open class CSMapClientController<V : View>(parent: CSViewController<V>, private val mapFrameId: Int,
                                           open val mapController: CSMapController)
    : CSViewController<V>(parent) {

    private var lastLocation: LatLng? = null
    private var lastZoom: Float? = null
    private val onMapShowingEvent: CSEvent<GoogleMap> = event()
    fun onMapShowing(function: (GoogleMap) -> Unit) = onMapShowingEvent.run { _, map -> function(map) }
    private val onMapClickEvent: CSEvent<LatLng> = event()
    fun onMapClick(function: (LatLng) -> Unit) = onMapClickEvent.run { _, location -> function(location) }
    private val onMapLongClickEvent: CSEvent<LatLng> = event()
    fun onMapLongClick(function: (LatLng) -> Unit) = onMapLongClickEvent.run { _, location -> function(location) }


    override fun onViewShowing() {
        super.onViewShowing()
        whileShowing(mapController.onMapAvailable { map ->
            //            mapController.view.hide()
//            doLater(SECOND) {
            frame(mapFrameId).add(mapController.view.removeFromSuperview(), layoutMatch)
//                mapController.view.fadeIn()
//            }
            map.clear()
            map.setOnMapClickListener { latLng -> onMapClickEvent.fire(latLng) }
            map.setOnMapLongClickListener { latLng -> onMapLongClickEvent.fire(latLng) }
            onMapShowingEvent.fire(map)
        })
        whileShowing(mapController.onCameraStopped { map ->
            lastLocation = map.cameraPosition.target
            lastZoom = map.cameraPosition.zoom
        })
        lastLocation?.let { latLng -> mapController.camera(latLng, lastZoom!!) }
    }

}


